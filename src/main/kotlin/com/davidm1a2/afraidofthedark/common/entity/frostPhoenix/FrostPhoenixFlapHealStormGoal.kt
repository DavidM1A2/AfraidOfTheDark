package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import net.minecraft.world.entity.player.Player
import java.time.Duration

class FrostPhoenixFlapHealStormGoal(frostPhoenix: FrostPhoenixEntity) : FrostPhoenixMoveBaseGoal(frostPhoenix) {
    private var nextStormTime = 0L
    private var stormStartTime = Int.MAX_VALUE

    override fun canUse(): Boolean {
        // This means the entity was unloaded while storming, and should resume storming
        if (phoenix.combatManager.isStorming()) {
            return true
        }
        if (phoenix.target == null) {
            // As long as we don't have a target, reset next storm time so once we get a target we don't storm right away
            nextStormTime = System.currentTimeMillis() + TIME_BETWEEN_STORMS.toMillis()
            return false
        }
        if (phoenix.stance != FrostPhoenixStance.FLYING) {
            return false
        }
        if (System.currentTimeMillis() < nextStormTime) {
            return false
        }

        return true
    }

    override fun canContinueToUse(): Boolean {
        // We can continue this goal if:
        // 1. The phoenix is flying or we're storming
        // 2. The phoenix has a target
        // 3. The phoenix was not hurt by a player while storming
        val phoenixIsFlyingOrStorming = phoenix.stance == FrostPhoenixStance.FLYING || phoenix.combatManager.isStorming()
        val phoenixHasTarget = phoenix.target != null
        val phoenixWasHurtByPlayerInStorm = phoenix.lastHurtByMobTimestamp >= stormStartTime && phoenix.lastHurtByMob is Player
        return phoenixIsFlyingOrStorming && phoenixHasTarget && !phoenixWasHurtByPlayerInStorm
    }

    override fun tick() {
        super.tick()

        // Try flying to the ground if we're in the FLYING state
        val position = phoenix.position()
        if (phoenix.stance == FrostPhoenixStance.FLYING) {
            val currentTargetPosition = getCurrentTargetPosition()
            flyTo(currentTargetPosition.x, position.y - 15, currentTargetPosition.z)
        }

        // Attempt to start storming if we're grounded
        if (phoenix.stance != FrostPhoenixStance.STORMING && (phoenix.isOnGround || position.y <= 1)) {
            phoenix.stance = FrostPhoenixStance.STORMING
            phoenix.combatManager.startStorming()
            stormStartTime = phoenix.tickCount
        }

        // If we're storming, tick it
        if (phoenix.stance == FrostPhoenixStance.STORMING) {
            phoenix.combatManager.tickStorming()
        }
    }

    override fun stop() {
        super.stop()
        nextStormTime = System.currentTimeMillis() + TIME_BETWEEN_STORMS.toMillis()
        stormStartTime = Int.MAX_VALUE
        phoenix.combatManager.stopStorming()
    }

    companion object {
        private val TIME_BETWEEN_STORMS = Duration.ofSeconds(30)
    }
}