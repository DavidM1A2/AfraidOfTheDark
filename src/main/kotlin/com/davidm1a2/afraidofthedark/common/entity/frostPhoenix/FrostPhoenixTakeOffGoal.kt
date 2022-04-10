package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

class FrostPhoenixTakeOffGoal(phoenix: FrostPhoenixEntity) : FrostPhoenixMoveBaseGoal(phoenix) {
    private var ticksUntilTakenOff = 0

    override fun canUse(): Boolean {
        if (phoenix.stance != FrostPhoenixStance.STANDING) {
            return false
        }
        // Take off if we have a target to attack
        if (phoenix.target != null) {
            return true
        }
        // Take off every ~20 seconds
        return phoenix.random.nextDouble() < 0.01
    }

    override fun tick() {
        super.tick()
        ticksUntilTakenOff = ticksUntilTakenOff - 1
        val currentTargetPosition = getCurrentTargetPosition()
        flyTo(currentTargetPosition.x, currentTargetPosition.y, currentTargetPosition.z)
    }

    override fun canContinueToUse(): Boolean {
        return ticksUntilTakenOff > 0
    }

    override fun start() {
        phoenix.stance = FrostPhoenixStance.TAKING_OFF
        ticksUntilTakenOff = TAKE_OFF_TICKS
    }

    companion object {
        private const val TAKE_OFF_TICKS = 18
    }
}