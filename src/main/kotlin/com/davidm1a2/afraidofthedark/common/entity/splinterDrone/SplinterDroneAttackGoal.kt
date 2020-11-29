package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacket
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.network.PacketDistributor

/**
 * AI task that allows the splinter drone to attack nearby players
 *
 * @constructor sets the entity that is shooting
 * @param splinterDrone The splinter drone entity
 * @property target The target that the drone is shooting at
 * @property timeUntilNextAttack The number of ticks until the next shots
 */
class SplinterDroneAttackGoal(private val splinterDrone: SplinterDroneEntity) : Goal() {
    private var target: LivingEntity? = null
    private var timeUntilNextAttack = 0

    /**
     * Execute if we have a target
     *
     * @return True if we have a target, false otherwise
     */
    override fun shouldExecute(): Boolean {
        // If the splinter drone is 'activating' don't attack yet (4 seconds)
        if (splinterDrone.ticksExisted < 80) {
            return false
        }

        // Grab the target entity
        val target = splinterDrone.attackTarget

        // If there is no target don't execute the attack
        return if (target == null) {
            false
        } else {
            this.target = target
            true
        }
    }

    /**
     * Continue executing if the target entity is valid and we can see it
     *
     * @return True if the attack should continue, false otherwise
     */
    override fun shouldContinueExecuting(): Boolean {
        return shouldExecute() && splinterDrone.canEntityBeSeen(target!!)
    }

    /**
     * Resets the task as if nothing had happened yet
     */
    override fun resetTask() {
        // Reset target and time until next attack
        target = null
        timeUntilNextAttack = TIME_BETWEEN_ATTACKS
    }

    /**
     * Called every tick to update the task and fire projectiles
     */
    override fun tick() {
        // Server side processing only
        if (!target!!.world.isRemote) {
            // Look at the target player
            splinterDrone.lookController.setLookPositionWithEntity(target!!, 30.0f, 30.0f)

            // Engage the player if there is a valid target (should always be true)
            if (splinterDrone.attackTarget != null) {
                // Play the charge animation if it is not already playing and activate is not already playing
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    AnimationPacket(splinterDrone, "Charge", "Activate", "Charge"),
                    PacketDistributor.TargetPoint(
                        splinterDrone.posX,
                        splinterDrone.posY,
                        splinterDrone.posZ,
                        50.0,
                        splinterDrone.dimension
                    )
                )
            }

            // If we are ready to attack do so
            if (timeUntilNextAttack-- <= 0) {
                // Compute the x, y, and z velocities of the projectile
                var xVelocity = target!!.posX - splinterDrone.posX
                var yVelocity =
                    target!!.boundingBox.minY + (target!!.height / 2.0f).toDouble() - (splinterDrone.posY + (splinterDrone.height / 2.0f).toDouble())
                var zVelocity = target!!.posZ - splinterDrone.posZ

                // Add random inaccuracy distributed over a gaussian with 0.4 block max inaccuracy
                xVelocity = xVelocity + splinterDrone.rng.nextGaussian() * 0.4 - 0.2
                yVelocity = yVelocity + splinterDrone.rng.nextGaussian() * 0.4 - 0.2
                zVelocity = zVelocity + splinterDrone.rng.nextGaussian() * 0.4 - 0.2

                // Create the projectile and spawn it in
                val attackShot = SplinterDroneProjectileEntity(splinterDrone.world, splinterDrone, xVelocity, yVelocity, zVelocity)
                splinterDrone.world.addEntity(attackShot)

                // Play the shoot fireball sound effect
                splinterDrone.world.playEvent(
                    null,
                    1018,
                    BlockPos(splinterDrone.posX, splinterDrone.posY, splinterDrone.posZ),
                    0
                )

                // Attack again in the future
                timeUntilNextAttack = TIME_BETWEEN_ATTACKS
            }
        }
    }

    companion object {
        // The number of ticks inbetween shots
        private const val TIME_BETWEEN_ATTACKS = 10
    }
}