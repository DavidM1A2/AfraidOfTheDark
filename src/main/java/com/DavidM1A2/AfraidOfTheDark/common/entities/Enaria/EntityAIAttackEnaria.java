/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIAttackEnaria extends EntityAIBase
{
	private EntityEnaria enaria;
	private EntityLivingBase target;
	private int attackTime = 0;

	public EntityAIAttackEnaria(EntityEnaria enaria)
	{
		this.enaria = enaria;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase target = this.enaria.getAttackTarget();

		if (target == null)
		{
			return false;
		}
		else
		{
			this.target = target;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		return this.shouldExecute() || !this.enaria.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.target = null;
		this.attackTime = 0;
	}

	/**
	 * Updates the task
	 */
	public void updateTask()
	{
		this.enaria.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);

		if (this.attackTime <= 0)
		{
			this.attackTime = this.enaria.worldObj.rand.nextInt(100) + 100;
			this.enaria.attackEntityWithRangedAttack(target, 0);
		}
		else
		{
			this.attackTime = this.attackTime - 1;
		}
	}
}