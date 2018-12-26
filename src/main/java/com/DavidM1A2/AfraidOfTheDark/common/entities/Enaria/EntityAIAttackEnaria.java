/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

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
			this.attackTime = this.enaria.worldObj.rand.nextInt(100) + 140;
			this.enaria.getEnariaAttacks().performRandomAttack();
			this.enaria.clearActivePotions();
			AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncAnimation("spell", this.enaria, "spell"), new TargetPoint(this.enaria.dimension, this.enaria.posX, this.enaria.posY, this.enaria.posZ, 100));
		}
		else if (this.attackTime % 40 == 0)
		{
			this.attackTime = this.attackTime - 1;
			if (!this.enaria.isPotionActive(Potion.getPotionById(14)))
			{
				this.enaria.getEnariaAttacks().performBasicAttack();
				AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncAnimation("autoattack", this.enaria, "spell", "autoattack"), new TargetPoint(this.enaria.dimension, this.enaria.posX, this.enaria.posY, this.enaria.posZ, 100));
			}
		}
		else
		{
			this.attackTime = this.attackTime - 1;
		}
	}
}