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
			this.target = enaria.world.getClosestPlayerToEntity(this.enaria, 500);

		if (this.target == null || target.isDead)
		{
			this.enaria.setDead();
		}
		else
		{
			if (!this.enaria.isBenign())
				this.enaria.getMoveHelper().setMoveTo(this.target.posX, this.target.posY, this.target.posZ, this.enaria.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
			this.enaria.faceEntity(this.target, 360f, 360f);
			if (this.target.canEntityBeSeen(this.enaria))
				this.target.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, 4, false, false));
		}
	}
}