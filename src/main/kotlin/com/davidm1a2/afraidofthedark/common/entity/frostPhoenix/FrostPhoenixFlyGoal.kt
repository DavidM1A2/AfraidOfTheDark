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
        if (ticksUntilLanding % 4 == 0) {
            updateFlyPosition()
        }
    }

    override fun canContinueToUse(): Boolean {
        return ticksUntilLanding > 0
    }

    override fun start() {
        phoenix.stance = FrostPhoenixStance.FLYING
        ticksUntilLanding = MIN_FLYING_TICKS + phoenix.random.nextInt(MAX_FLYING_TICKS - MIN_FLYING_TICKS)
        updateFlyPosition()
    }

    private fun updateFlyPosition() {
        val spawnerPos = phoenix.spawnerPos
        val ticksAlive = phoenix.tickCount
        val x = spawnerPos.x + 0.5 + sin(ticksAlive / 40.0) * FLY_DIAMETER
        val y = spawnerPos.y + 0.5 + MAX_FLY_HEIGHT
        val z = spawnerPos.z + 0.5 + cos(ticksAlive / 40.0) * FLY_DIAMETER
        flyTo(x, y, z)
    }

    companion object {
        private const val MIN_FLYING_TICKS = 20 * 15
        private const val MAX_FLYING_TICKS = 20 * 25
        private const val FLY_DIAMETER = 40
    }
}