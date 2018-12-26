/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIFollowTarget extends EntityAIBase
{
	private EntityLiving entity;
	private EntityPlayer target;
	private double range;
	private static final int MAX_RANGE = 256;
	private static final int TRACK_RANGE = 32;
	private int timeSinceLastUpdate = 0;

	public EntityAIFollowTarget(EntityLiving entity, double range)
	{
		this.entity = entity;
		this.range = range;
	}

	@Override
	public boolean shouldExecute()
	{
		List list = this.entity.world.getEntitiesWithinAABB(EntityPlayer.class, this.entity.getEntityBoundingBox().expand(TRACK_RANGE, TRACK_RANGE, TRACK_RANGE));
		EntityPlayer entityPlayer = null;
		double minDistance = Double.MAX_VALUE;
		Iterator iterator = list.iterator();

		while (iterator.hasNext())
		{
			EntityPlayer entityPlayerCurrent = (EntityPlayer) iterator.next();

			double distance = this.entity.getDistanceToEntity(entityPlayerCurrent);

			if (distance <= minDistance)
			{
				if (!entityPlayerCurrent.capabilities.isCreativeMode)
				{
					minDistance = distance;
					entityPlayer = entityPlayerCurrent;
				}
			}
		}

		if (entityPlayer == null)
		{
			return false;
		}
		else if (minDistance < range)
		{
			return false;
		}
		else
		{
			this.target = entityPlayer;
			return true;
		}
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		this.entity.getNavigator().clearPathEntity();
		this.target = null;
	}

	@Override
	public void startExecuting()
	{
		this.timeSinceLastUpdate = 0;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		if (!this.target.isEntityAlive() || this.target.capabilities.isCreativeMode)
		{
			return false;
		}
		else
		{
			double distance = this.entity.getDistanceToEntity(this.target);
			return distance >= range && distance <= MAX_RANGE;
		}
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		this.timeSinceLastUpdate = this.timeSinceLastUpdate - 1;
		if (this.timeSinceLastUpdate <= 0)
		{
			this.timeSinceLastUpdate = 10;
			this.entity.getNavigator().tryMoveToEntityLiving(this.target, this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
		}
	}
}
