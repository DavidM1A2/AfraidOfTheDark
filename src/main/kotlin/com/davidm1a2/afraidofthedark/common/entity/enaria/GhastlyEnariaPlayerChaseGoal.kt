package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import java.util.EnumSet

/**
 * Ghastly enaria task that lets her chase players
 *
 * @property enaria A reference to the enaria entity
 * @property targetPlayer The target player enaria is chasing
 */
class GhastlyEnariaPlayerChaseGoal(private val enaria: GhastlyEnariaEntity) : Goal() {
    private var targetPlayer: PlayerEntity? = null

    init {
        // Sync movement, look, and etc state
        flags = EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET)
    }

    /**
     * This will only execute if enaria is not moving
     */
    override fun canUse(): Boolean {
        return !enaria.moveControl.hasWanted()
    }

    /**
     * False since the ai task only executes once
     */
    override fun canContinueToUse(): Boolean {
        return false
    }

    /**
     * Executes the task. Finds a nearby player and follows them
     */
    override fun start() {
        val distanceBetweenIslands = Constants.DISTANCE_BETWEEN_ISLANDS

        // If the target player is null try and find a nearby player
        if (targetPlayer == null) {
            targetPlayer = enaria.level.getNearestPlayer(enaria, distanceBetweenIslands / 2.0)
        }

        // If there are no players nearby kill enaria
        if (targetPlayer == null || !targetPlayer!!.isAlive) {
            enaria.remove()
        } else {
            // Otherwise follow the player if not benign
            if (!enaria.isBenign()) {
                // Move to the player
                enaria.moveControl.setWantedPosition(
                    targetPlayer!!.x,
                    targetPlayer!!.y,
                    targetPlayer!!.z,
                    enaria.getAttribute(Attributes.MOVEMENT_SPEED)!!.value
                )
            }

            // Face the player entity always
            enaria.lookAt(targetPlayer!!, 360f, 360f)

            // If the player can see enaria, add slowness 4 to the player
            if (!enaria.isBenign() && targetPlayer!!.canSee(enaria)) {
                targetPlayer!!.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 4, false, false))
            }
        }
    }
}