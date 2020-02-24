package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

/**
 * Ghastly enaria task that lets her chase players
 *
 * @property enaria A reference to the enaria entity
 * @property targetPlayer The target player enaria is chasing
 */
class GhastlyEnariaPlayerChase(private val enaria: EntityGhastlyEnaria) : EntityAIBase() {
    private var targetPlayer: EntityPlayer? = null

    init {
        // Sync movement, look, and etc state
        mutexBits = 1 or 2 or 4
    }

    /**
     * This will only execute if enaria is not moving
     */
    override fun shouldExecute(): Boolean {
        return !enaria.moveHelper.isUpdating
    }

    /**
     * False since the ai task only executes once
     */
    override fun shouldContinueExecuting(): Boolean {
        return false
    }

    /**
     * Executes the task. Finds a nearby player and follows them
     */
    override fun startExecuting() {
        val distanceBetweenIslands = AfraidOfTheDark.INSTANCE.configurationHandler.blocksBetweenIslands

        // If the target player is null try and find a nearby player
        if (targetPlayer == null) {
            targetPlayer = enaria.world.getClosestPlayerToEntity(enaria, distanceBetweenIslands / 2.toDouble())
        }

        // If there are no players nearby kill enaria
        if (targetPlayer == null || targetPlayer!!.isDead) {
            enaria.setDead()
        } else {
            // Otherwise follow the player if not benign
            if (!enaria.isBenign()) {
                // Move to the player
                enaria.moveHelper
                    .setMoveTo(
                        targetPlayer!!.posX,
                        targetPlayer!!.posY,
                        targetPlayer!!.posZ,
                        enaria.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).attributeValue
                    )
            }

            // Face the player entity always
            enaria.faceEntity(targetPlayer!!, 360f, 360f)

            // If the player can see enaria, add slowness 4 to the player
            if (targetPlayer!!.canEntityBeSeen(enaria)) {
                targetPlayer!!.addPotionEffect(PotionEffect(Potion.getPotionById(2)!!, 60, 4, false, false))
            }
        }
    }
}