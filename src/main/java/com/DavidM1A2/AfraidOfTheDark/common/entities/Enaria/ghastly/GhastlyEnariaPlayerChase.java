/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class GhastlyEnariaPlayerChase extends EntityAIBase
{
	private EntityGhastlyEnaria enaria;
	private EntityPlayer target = null;

	public GhastlyEnariaPlayerChase(EntityGhastlyEnaria enaria)
	{
		this.enaria = enaria;
		this.setMutexBits(7);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityMoveHelper entitymovehelper = this.enaria.getMoveHelper();
		if (!entitymovehelper.isUpdating())
			return true;
		else
			return false;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		if (this.target == null)
			this.target = enaria.worldObj.getClosestPlayerToEntity(this.enaria, 500);

		if (this.target == null || target.isDead)
		{
			this.enaria.setDead();
		}
		else
		{
			if (!this.enaria.isBenign())
			{
				this.enaria.getMoveHelper().setMoveTo(this.target.posX, this.target.posY, this.target.posZ, this.enaria.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
			}

			/*
			double d1 = target.posX - this.enaria.posX;
			double d2 = target.posZ - this.enaria.posZ;
			this.enaria.renderYawOffset = this.enaria.rotationYaw = -((float) MathHelper.atan2(d1, d2)) * 180.0F / (float) Math.PI;
			*/

			if (!this.enaria.worldObj.isRemote)
			{
				if (this.target.canEntityBeSeen(this.enaria))
				{
					this.target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 60, 4, false, false));
				}
			}
		}
	}
}