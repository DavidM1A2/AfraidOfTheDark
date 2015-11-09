package com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityAILookIdleSplinterDrone extends EntityAIBase
{
	 /** The entity that is looking idle. */
    private EntitySplinterDrone idleEntitySplinterDrone;
    /** X offset to look at */
    private double lookX;
    /** Z offset to look at */
    private double lookZ;
    /** A decrementing tick that stops the entity from being idle once it reaches 0. */
    private int idleTime;

    public EntityAILookIdleSplinterDrone(EntitySplinterDrone entitySplinterDrone)
    {
        this.idleEntitySplinterDrone = entitySplinterDrone;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return this.idleEntitySplinterDrone.getRNG().nextFloat() < 0.02F;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.idleTime >= 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        double d0 = (Math.PI * 2D) * this.idleEntitySplinterDrone.getRNG().nextDouble();
        this.lookX = Math.cos(d0);
        this.lookZ = Math.sin(d0);
        this.idleTime = 20 + this.idleEntitySplinterDrone.getRNG().nextInt(20);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
    	AnimationHandler animationHandler = this.idleEntitySplinterDrone.getAnimationHandler();
    	if (!animationHandler.isAnimationActive("Activate") && !animationHandler.isAnimationActive("Charge") && !animationHandler.isAnimationActive("Idle"))
    	{
    		animationHandler.activateAnimation("Idle", 0);
    		AfraidOfTheDark.getPacketHandler().sendToAllAround(new SyncAnimation("Idle", this.idleEntitySplinterDrone.getEntityId()), new TargetPoint(this.idleEntitySplinterDrone.dimension, this.idleEntitySplinterDrone.posX, this.idleEntitySplinterDrone.posY, this.idleEntitySplinterDrone.posZ, 50));
    	}
        --this.idleTime;
        this.idleEntitySplinterDrone.getLookHelper().setLookPosition(this.idleEntitySplinterDrone.posX + this.lookX, this.idleEntitySplinterDrone.posY + (double)this.idleEntitySplinterDrone.getEyeHeight(), this.idleEntitySplinterDrone.posZ + this.lookZ, 10.0F, (float)this.idleEntitySplinterDrone.getVerticalFaceSpeed());
    }
}
