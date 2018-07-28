/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class CustomWerewolfTargetLocator extends EntityAITarget
{
	protected final Class targetClass;
	private final int targetChance;
	/** Instance of EntityAINearestAttackableTargetSorter. */
	protected final CustomWerewolfTargetLocator.Sorter theCustomWerewolfTargetLocatorSorter;
	/**
	 * This filter is applied to the Entity search. Only matching entities will
	 * be targetted. (null -> no restrictions)
	 */
	protected Predicate targetEntitySelector;
	protected EntityLivingBase targetEntity;

	public CustomWerewolfTargetLocator(final EntityCreature entityCreature, final Class target, final int targetChance, final boolean shouldCheckSight)
	{
		this(entityCreature, target, targetChance, shouldCheckSight, false);
	}

	public CustomWerewolfTargetLocator(final EntityCreature entityCreature, final Class target, final int targetChance, final boolean shouldCheckSight, final boolean nearbyOnly)
	{
		this(entityCreature, target, targetChance, shouldCheckSight, nearbyOnly, (Predicate) null);
	}

	public CustomWerewolfTargetLocator(final EntityCreature entityCreature, final Class target, final int targetChance, final boolean shouldCheckSight, final boolean nearbyOnly, final Predicate targetSelector)
	{
		// Call the superclass's constructor.
		super(entityCreature, shouldCheckSight, nearbyOnly);
		// Set the target, targetChance, sorter, and mutex bits (defines ai
		// behavior)
		this.targetClass = target;
		this.targetChance = targetChance;
		this.theCustomWerewolfTargetLocatorSorter = new CustomWerewolfTargetLocator.Sorter(entityCreature);
		this.setMutexBits(1);
		// To select a target we run custom code (check HasStartedAOTD)
		this.targetEntitySelector = new Predicate()
		{
			/**
			 * Return whether the specified entity is applicable to this filter.
			 */
			public boolean isEntityApplicable(final EntityLivingBase entity)
			{
				if ((targetSelector != null) && !targetSelector.apply(entity))
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

							d0 *= 0.7F * f;
						}

						if (entity.getDistance(CustomWerewolfTargetLocator.this.taskOwner) > d0)
						{
							return false;
						}
					}

					return CustomWerewolfTargetLocator.this.isSuitableTarget(entity, false);
				}
			}

			@Override
			public boolean apply(final Object input)
			{
				return this.isEntityApplicable((EntityLivingBase) input);
			}
		};
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if ((this.targetChance > 0) && (this.taskOwner.getRNG().nextInt(this.targetChance) != 0))
		{
			return false;
		}
		else
		{
			final double d0 = this.getTargetDistance();
			final List list = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), this.targetEntitySelector);
			Collections.sort(list, this.theCustomWerewolfTargetLocatorSorter);

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
						if (((EntityPlayer) list.get(i)).getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD() || ((EntityWerewolf) this.taskOwner).canAttackAnyone())
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
	@Override
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}

	public static class Sorter implements Comparator
	{
		private final Entity theEntity;

		public Sorter(final Entity p_i1662_1_)
		{
			this.theEntity = p_i1662_1_;
		}

		public int compare(final Entity p_compare_1_, final Entity p_compare_2_)
		{
			final double d0 = this.theEntity.getDistanceSq(p_compare_1_);
			final double d1 = this.theEntity.getDistanceSq(p_compare_2_);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}

		@Override
		public int compare(final Object p_compare_1_, final Object p_compare_2_)
		{
			return this.compare((Entity) p_compare_1_, (Entity) p_compare_2_);
		}
	}
}
