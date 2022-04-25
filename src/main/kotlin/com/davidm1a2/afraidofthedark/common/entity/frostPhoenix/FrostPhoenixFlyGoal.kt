package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

class FrostPhoenixFlyGoal(phoenix: FrostPhoenixEntity) : FrostPhoenixMoveBaseGoal(phoenix) {
    private var ticksUntilLanding = 0

    override fun canUse(): Boolean {
        return phoenix.stance == FrostPhoenixStance.TAKING_OFF
    }

    override fun tick() {
        super.tick()
        ticksUntilLanding = ticksUntilLanding - 1
        val currentTargetPosition = getCurrentTargetPosition()
        flyTo(currentTargetPosition.x, currentTargetPosition.y, currentTargetPosition.z)
    }

    override fun canContinueToUse(): Boolean {
        // Use it if we have flying ticks left or we're attacking a target
        return ticksUntilLanding > 0 || phoenix.target != null
    }

    override fun start() {
        super.start()
        phoenix.stance = FrostPhoenixStance.FLYING
        ticksUntilLanding = MIN_FLYING_TICKS + phoenix.random.nextInt(MAX_FLYING_TICKS - MIN_FLYING_TICKS)
        val currentTargetPosition = getCurrentTargetPosition()
        flyTo(currentTargetPosition.x, currentTargetPosition.y, currentTargetPosition.z)
    }

    companion object {
        private const val MIN_FLYING_TICKS = 20 * 15
        private const val MAX_FLYING_TICKS = 20 * 25
        internal const val FLY_DIAMETER = 40
    }
}