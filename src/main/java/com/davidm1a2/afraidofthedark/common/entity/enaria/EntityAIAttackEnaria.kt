package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.potion.Potion
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint

/**
 * Class that allows enaria to attack
 *
 * @property enaria The enaria entity
 * @property target The target enaria is hitting
 * @property ticksUntilNextAttack The ticks until enarias next attack
 */
class EntityAIAttackEnaria(private val enaria: EntityEnaria) : EntityAIBase()
{
    private var target: EntityLivingBase? = null
    private var ticksUntilNextAttack = 0

    /**
     * Returns whether the EntityAIBase should begin execution.
     *
     * @return True if the target is non-null, false otherwise
     */
    override fun shouldExecute(): Boolean
    {
        // Get enaria's target
        val target = enaria.attackTarget

        // if there's no target don't execute, otherwise set the target
        return if (target == null)
        {
            false
        }
        else
        {
            this.target = target
            true
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     *
     * @return True if there is no target or there is no path to the entity
     */
    override fun shouldContinueExecuting(): Boolean
    {
        return shouldExecute() || !enaria.navigator.noPath()
    }

    /**
     * Resets the task
     */
    override fun resetTask()
    {
        target = null
        ticksUntilNextAttack = 0
    }

    /**
     * Updates the task and attacks the target
     */
    override fun updateTask()
    {
        // Look at the target
        enaria.lookHelper.setLookPositionWithEntity(target!!, 30.0f, 30.0f)

        // Decrease the ticks until next attack
        ticksUntilNextAttack = ticksUntilNextAttack - 1

        // If ticks until next attack is 0 then perform a spell attack
        if (ticksUntilNextAttack <= 0)
        {
            // The next spell will be in the next 7 and 13 seconds
            ticksUntilNextAttack = enaria.world.rand.nextInt(100) + 140

            // Perform a random spell attack
            enaria.enariaAttacks.performRandomSpell()

            // Make sure you can't use potions on enaria, she clears them every spell cast
            enaria.clearActivePotions()

            // Play the spell cast animation
            AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
                SyncAnimation("spell", enaria, "spell"),
                TargetPoint(enaria.dimension, enaria.posX, enaria.posY, enaria.posZ, 100.0)
            )
        }
        else if (ticksUntilNextAttack % 40 == 0)
        {
            // If Enaria is not invisible basic attack
            if (!enaria.isPotionActive(Potion.getPotionById(14)!!))
            {
                // Perform basic attack
                enaria.enariaAttacks.performBasicAttack()

                // Show the auto attack animation
                AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
                    SyncAnimation("autoattack", enaria, "spell", "autoattack"),
                    TargetPoint(enaria.dimension, enaria.posX, enaria.posY, enaria.posZ, 100.0)
                )
            }
        }
    }
}