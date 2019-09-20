package com.davidm1a2.afraidofthedark.common.entity.splinterDrone;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * AI task that allows the splinter drone to attack nearby players
 */
public class EntityAIAttackSplinterDrone extends EntityAIBase
{
    // The splinter drone entity
    private EntitySplinterDrone splinterDrone;
    // The target that the drone is shooting at
    private EntityLivingBase target;
    // The number of ticks until the next shots
    private int timeUntilNextAttack = 0;
    // The number of ticks inbetween shots
    private static final int TIME_BETWEEN_ATTACKS = 10;

    /**
     * Constructor sets the entity that is shooting
     *
     * @param splinterDrone The splinter drone entity
     */
    public EntityAIAttackSplinterDrone(EntitySplinterDrone splinterDrone)
    {
        this.splinterDrone = splinterDrone;
    }

    /**
     * Execute if we have a target
     *
     * @return True if we have a target, false otherwise
     */
    @Override
    public boolean shouldExecute()
    {
        // Grab the target entity
        EntityLivingBase target = this.splinterDrone.getAttackTarget();
        // If there is no target don't execute the attack
        if (target == null)
        {
            return false;
        }
        // Otherwise attack
        else
        {
            this.target = target;
            return true;
        }
    }

    /**
     * Continue executing if the target entity is valid and we can see it
     *
     * @return True if the attack should continue, false otherwise
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return this.shouldExecute() && this.splinterDrone.canEntityBeSeen(this.target);
    }

    /**
     * Resets the task as if nothing had happened yet
     */
    @Override
    public void resetTask()
    {
        // Reset target and time until next attack
        this.target = null;
        this.timeUntilNextAttack = TIME_BETWEEN_ATTACKS;
    }

    /**
     * Called every tick to update the task and fire projectiles
     */
    @Override
    public void updateTask()
    {
        // Server side processing only
        if (!this.target.world.isRemote)
        {
            // Look at the target player
            this.splinterDrone.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);

            // Engage the player if there is a valid target (should always be true)
            if (this.splinterDrone.getAttackTarget() != null)
            {
                // Play the charge animation if it is not already playing and activate is not already playing
                AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(new SyncAnimation("Charge", this.splinterDrone, "Activate", "Charge"), new NetworkRegistry.TargetPoint(this.splinterDrone.dimension, this.splinterDrone.posX, this.splinterDrone.posY, this.splinterDrone.posZ, 50));
            }

            // If we are ready to attack do so
            if (this.timeUntilNextAttack-- <= 0)
            {
                // Compute the x, y, and z velocities of the projectile
                double xVelocity = this.target.posX - this.splinterDrone.posX;
                double yVelocity = this.target.getEntityBoundingBox().minY + (double) (target.height / 2.0F) - (this.splinterDrone.posY + (double) (this.splinterDrone.height / 2.0F));
                double zVelocity = this.target.posZ - this.splinterDrone.posZ;

                // Add random inaccuracy distributed over a gaussian with 0.4 block max inaccuracy
                xVelocity = xVelocity + this.splinterDrone.getRNG().nextGaussian() * 0.4;
                yVelocity = yVelocity + this.splinterDrone.getRNG().nextGaussian() * 0.4;
                zVelocity = zVelocity + this.splinterDrone.getRNG().nextGaussian() * 0.4;

                // Create the projectile and spawn it in
                Entity attackShot = new EntitySplinterDroneProjectile(this.splinterDrone.world, this.splinterDrone, xVelocity, yVelocity, zVelocity);
                this.splinterDrone.world.spawnEntity(attackShot);
                // Play the shoot fireball sound effect
                this.splinterDrone.world.playEvent(null, 1018, new BlockPos(this.splinterDrone.posX, this.splinterDrone.posY, this.splinterDrone.posZ), 0);
                // Attack again in the future
                this.timeUntilNextAttack = TIME_BETWEEN_ATTACKS;
            }
        }
    }
}
