/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.AI;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;

public class CustomWerewolfTargetLocator extends EntityAITarget
{
	private final Class targetClass;
	private final int targetChance;
	/** Instance of EntityAINearestAttackableTargetSorter. */
	private final CustomWerewolfTargetLocator.Sorter theNearestAttackableTargetSorter;
	/**
	 * This filter is applied to the Entity search. Only matching entities will
	 * be targetted. (null -> no restrictions)
	 */
	private final IEntitySelector targetEntitySelector;
	private EntityLivingBase targetEntity;

	public CustomWerewolfTargetLocator(EntityCreature entityCreature, Class target, int targetChance, boolean shouldCheckSight)
	{
		this(entityCreature, target, targetChance, shouldCheckSight, false);
	}

	public CustomWerewolfTargetLocator(EntityCreature entityCreature, Class target, int targetChance, boolean shouldCheckSight, boolean nearbyOnly)
	{
		this(entityCreature, target, targetChance, shouldCheckSight, nearbyOnly, (IEntitySelector) null);
	}

	public CustomWerewolfTargetLocator(EntityCreature entityCreature, Class target, int targetChance, boolean shouldCheckSight, boolean nearbyOnly, final IEntitySelector iEntitySelector)
	{
		// Call the superclass's constructor.
		super(entityCreature, shouldCheckSight, nearbyOnly);
		// Set the target, targetChance, sorter, and mutex bits (defines ai
		// behavior)
		this.targetClass = target;
		this.targetChance = targetChance;
		this.theNearestAttackableTargetSorter = new CustomWerewolfTargetLocator.Sorter(entityCreature);
		this.setMutexBits(1);
		// To select a target we run custom code (check HasStartedAOTD)
		this.targetEntitySelector = new IEntitySelector()
		{
			/**
			 * Return whether the specified entity is applicable to this filter.
			 */
			public boolean isEntityApplicable(Entity entity)
			{
				return !(entity instanceof EntityLivingBase) ? false : (iEntitySelector != null && !iEntitySelector.isEntityApplicable(entity) ? false : CustomWerewolfTargetLocator.this.isSuitableTarget((EntityLivingBase) entity, false));
			}
		};
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
		{
			return false;
		}
		else
		{
			double d0 = this.getTargetDistance();
			List list = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand(d0, 4.0D, d0), this.targetEntitySelector);
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			if (list.isEmpty())
			{
				return false;
			}
			else
			{
				/*
				 * Here we added some extra code to check if the target is a
				 * person who has started AOTD and we ignore all other players
				 */
				for (int i = 0; i < list.size(); i++)
				{
					if (list.get(i) instanceof EntityPlayer)
					{
						if (HasStartedAOTD.get((EntityPlayer) list.get(i)))
						{
							/*
							 * The first entity in the list that has started
							 * AOTD is the target
							 */
							this.targetEntity = (EntityLivingBase) list.get(i);
							return true;
						}
					}
				}
				return false;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}

	// Compare two entity's distance
	public static class Sorter implements Comparator
	{
		private final Entity theEntity;
		private static final String __OBFID = "CL_00001622";

		public Sorter(Entity p_i1662_1_)
		{
			this.theEntity = p_i1662_1_;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_)
		{
			double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
			double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}

		public int compare(Object p_compare_1_, Object p_compare_2_)
		{
			return this.compare((Entity) p_compare_1_, (Entity) p_compare_2_);
		}
	}

}
