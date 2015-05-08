/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.AI;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class CustomWerewolfTargetLocator extends EntityAITarget
{
	protected final Class targetClass;
	private final int targetChance;
	/** Instance of EntityAINearestAttackableTargetSorter. */
	protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	/**
	 * This filter is applied to the Entity search. Only matching entities will be targetted. (null -> no restrictions)
	 */
	protected Predicate targetEntitySelector;
	protected EntityLivingBase targetEntity;

	public CustomWerewolfTargetLocator(EntityCreature entityCreature, Class target, int targetChance, boolean shouldCheckSight)
	{
		this(entityCreature, target, targetChance, shouldCheckSight, false);
	}

	public CustomWerewolfTargetLocator(EntityCreature entityCreature, Class target, int targetChance, boolean shouldCheckSight, boolean nearbyOnly)
	{
		this(entityCreature, target, targetChance, shouldCheckSight, nearbyOnly, (Predicate) null);
	}

	public CustomWerewolfTargetLocator(EntityCreature entityCreature, Class target, int targetChance, boolean shouldCheckSight, boolean nearbyOnly, final Predicate targetSelector)
	{
		// Call the superclass's constructor.
		super(entityCreature, shouldCheckSight, nearbyOnly);
		// Set the target, targetChance, sorter, and mutex bits (defines ai
		// behavior)
		this.targetClass = target;
		this.targetChance = targetChance;
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(entityCreature);
		this.setMutexBits(1);
		// To select a target we run custom code (check HasStartedAOTD)
		this.targetEntitySelector = new Predicate()
		{
			/**
			 * Return whether the specified entity is applicable to this filter.
			 */
			public boolean isEntityApplicable(Entity entity)
			{
				if (targetSelector != null && !targetSelector.apply(entity))
				{
					return false;
				}
				else
				{
					if (entity instanceof EntityPlayer)
					{
						double d0 = CustomWerewolfTargetLocator.this.getTargetDistance();

						if (entity.isSneaking())
						{
							d0 *= 0.800000011920929D;
						}

						if (entity.isInvisible())
						{
							float f = ((EntityPlayer) entity).getArmorVisibility();

							if (f < 0.1F)
							{
								f = 0.1F;
							}

							d0 *= (double) (0.7F * f);
						}

						if ((double) entity.getDistanceToEntity(CustomWerewolfTargetLocator.this.taskOwner) > d0)
						{
							return false;
						}
					}

					return CustomWerewolfTargetLocator.this.isSuitableTarget((EntityLivingBase) entity, false);
				}
				// return !(entity instanceof EntityLivingBase) ? false : (iEntitySelector != null && !iEntitySelector.isEntityApplicable(entity) ?
				// false : CustomWerewolfTargetLocator.this.isSuitableTarget((EntityLivingBase) entity, false));
			}

			@Override
			public boolean apply(Object input)
			{
				return this.isEntityApplicable((Entity) input);
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
			List list = this.taskOwner.worldObj.func_175647_a(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), Predicates.and(this.targetEntitySelector, IEntitySelector.NOT_SPECTATING));
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			if (list.isEmpty())
			{
				return false;
			}
			else
			{
				/*
				 * Here we added some extra code to check if the target is a person who has started AOTD and we ignore all other players
				 */
				for (int i = 0; i < list.size(); i++)
				{
					if (list.get(i) instanceof EntityPlayer)
					{
						if (HasStartedAOTD.get((EntityPlayer) list.get(i)))
						{
							/*
							 * The first entity in the list that has started AOTD is the target
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
}
