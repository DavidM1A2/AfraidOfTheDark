package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import net.minecraft.world.entity.ai.goal.Goal

class FrostPhoenixProjectileAttackGoal(private val frostPhoenix: FrostPhoenixEntity) : Goal() {
    override fun canUse(): Boolean {
        if (frostPhoenix.stance != FrostPhoenixStance.FLYING) {
            return false
        }
        if (frostPhoenix.target == null) {
            return false
        }

        return frostPhoenix.tickCount % TIME_BETWEEN_ATTACKS == 0
    }

    override fun canContinueToUse(): Boolean {
        return false
    }

    override fun start() {
        frostPhoenix.combatManager.shootFireballAtTarget()
    }

    companion object {
        // The number of ticks inbetween shots (5 seconds)
        private const val TIME_BETWEEN_ATTACKS = 20 * 5
    }
}