package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import kotlin.math.cos
import kotlin.math.sin

class FrostPhoenixFlyGoal(phoenix: FrostPhoenixEntity) : FrostPhoenixMoveBaseGoal(phoenix) {
    private var ticksUntilLanding = 0

    override fun canUse(): Boolean {
        return phoenix.stance == FrostPhoenixStance.TAKING_OFF
    }

    override fun tick() {
        super.tick()
        ticksUntilLanding = ticksUntilLanding - 1
        updateFlyPosition()
    }

    override fun canContinueToUse(): Boolean {
        // Use it if we have flying ticks left or we're attacking a target
        return ticksUntilLanding > 0 || phoenix.target != null
    }

    override fun start() {
        phoenix.stance = FrostPhoenixStance.FLYING
        ticksUntilLanding = MIN_FLYING_TICKS + phoenix.random.nextInt(MAX_FLYING_TICKS - MIN_FLYING_TICKS)
        updateFlyPosition()
    }

    private fun updateFlyPosition() {
        val spawnerPos = phoenix.spawnerPos
        val ticksAlive = phoenix.tickCount
        val currentTarget = phoenix.target
        val centerX = currentTarget?.x ?: (spawnerPos.x + 0.5)
        val centerY = currentTarget?.y ?: (spawnerPos.y + 0.5)
        val centerZ = currentTarget?.z ?: (spawnerPos.z + 0.5)

        val x = centerX + sin(ticksAlive / 45.0) * FLY_DIAMETER
        val y = centerY + MAX_FLY_HEIGHT
        val z = centerZ + cos(ticksAlive / 45.0) * FLY_DIAMETER
        flyTo(x, y, z)
    }

    companion object {
        private const val MIN_FLYING_TICKS = 20 * 15
        private const val MAX_FLYING_TICKS = 20 * 25
        internal const val FLY_DIAMETER = 40
    }
}