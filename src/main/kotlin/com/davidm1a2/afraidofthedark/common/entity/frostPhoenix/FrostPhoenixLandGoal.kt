package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

class FrostPhoenixLandGoal(phoenix: FrostPhoenixEntity) : FrostPhoenixMoveBaseGoal(phoenix) {
    private var ticksUntilGivingUp = 0

    override fun canUse(): Boolean {
        return phoenix.stance == FrostPhoenixStance.FLYING
    }

    override fun tick() {
        super.tick()
        ticksUntilGivingUp = ticksUntilGivingUp - 1
        if (phoenix.stance == FrostPhoenixStance.FLYING && getDistanceToLandingSpotSquared() < 100) {
            phoenix.stance = FrostPhoenixStance.LANDING
        }
        val spawnerPos = phoenix.spawnerPos
        lookAt(spawnerPos.x + 0.5, spawnerPos.y + 1.0, spawnerPos.z + 0.5)
    }

    override fun canContinueToUse(): Boolean {
        return ticksUntilGivingUp > 0 && getDistanceToLandingSpotSquared() > 3.0
    }

    override fun start() {
        ticksUntilGivingUp = MAX_TIME_TO_LAND
        val spawnerPos = phoenix.spawnerPos
        val x = spawnerPos.x + 0.5
        val y = spawnerPos.y + 1.0
        val z = spawnerPos.z + 0.5
        flyTo(x, y, z)
    }

    override fun stop() {
        phoenix.stance = FrostPhoenixStance.STANDING
    }

    private fun getDistanceToLandingSpotSquared(): Double {
        val spawnerPos = phoenix.spawnerPos
        val x = spawnerPos.x + 0.5
        val y = spawnerPos.y + 1.0
        val z = spawnerPos.z + 0.5
        return phoenix.position().distanceToSqr(x, y, z)
    }

    companion object {
        private const val MAX_TIME_TO_LAND = 20 * 10
    }
}