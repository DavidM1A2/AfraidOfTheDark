
package com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityAIAttackSplinterDrone extends EntityAIBase
{
	private EntitySplinterDrone splinterDrone;
	private EntityLivingBase target;
	private int attackTime = 0;
	private static final int TIME_BETWEEN_ATTACKS = 10;

	public EntityAIAttackSplinterDrone(EntitySplinterDrone splinterDrone)
	{
		this.splinterDrone = splinterDrone;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		EntityLivingBase target = this.splinterDrone.getAttackTarget();
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
	@Override
	public boolean continueExecuting()
	{
		return this.shouldExecute() && this.splinterDrone.canEntityBeSeen(this.target);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		this.target = null;
		this.attackTime = 0;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		this.splinterDrone.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);

		if (!this.splinterDrone.worldObj.isRemote)
		{
			AnimationHandler animationHandler = this.splinterDrone.getAnimationHandler();
			if (!animationHandler.isAnimationActive("Activate") && !animationHandler.isAnimationActive("Charge"))
			{
				if (this.splinterDrone.getAttackTarget() != null)
				{
					animationHandler.activateAnimation("Charge", 0);
					AfraidOfTheDark.getPacketHandler().sendToAllAround(new SyncAnimation("Charge", this.splinterDrone.getEntityId()), new TargetPoint(this.splinterDrone.dimension, this.splinterDrone.posX, this.splinterDrone.posY, this.splinterDrone.posZ, 50));
				}
			}			
			
			if (this.attackTime <= 0)
			{
				float force = MathHelper.sqrt_float(MathHelper.sqrt_double(this.splinterDrone.getDistanceSqToEntity(this.target))) * 0.5F;
				double xVelocity = this.target.posX - this.splinterDrone.posX;
				double yVelocity = this.target.getEntityBoundingBox().minY + (double) (target.height / 2.0F) - (this.splinterDrone.posY + (double) (this.splinterDrone.height / 2.0F));
				double zVelocity = this.target.posZ - this.splinterDrone.posZ;

				this.splinterDrone.worldObj.playAuxSFXAtEntity(null, 1009, new BlockPos((int) this.splinterDrone.posX, (int) this.splinterDrone.posY, (int) this.splinterDrone.posZ), 0);
				//EntitySmallFireball fireballAttack = new EntitySmallFireball(this.splinterDrone.worldObj, this.splinterDrone, xVelocity, yVelocity, zVelocity);
				//fireballAttack.posY = this.splinterDrone.posY + (double) (this.splinterDrone.height / 2.0F) + 0.5D;
				//this.splinterDrone.worldObj.spawnEntityInWorld(fireballAttack);

				this.attackTime = TIME_BETWEEN_ATTACKS;
			}
			else
			{
				this.attackTime = this.attackTime - 1;
			}
		}
	}
}
