package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import net.minecraft.entity.ai.goal.Goal

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
        val target = frostPhoenix.target!!

        val shootPosition = frostPhoenix.position()
            .add(frostPhoenix.lookAngle.scale(frostPhoenix.boundingBox.zsize))
            .add(0.0, frostPhoenix.eyeHeight.toDouble(), 0.0)

        val targetPosition = target.position()
            .add(0.0, target.eyeHeight.toDouble(), 0.0)

        val direction = targetPosition.subtract(shootPosition).normalize()
        val projectile = FrostPhoenixProjectileEntity(frostPhoenix, direction.x, direction.y, direction.z)
        projectile.setPos(shootPosition.x, shootPosition.y, shootPosition.z)
        frostPhoenix.level.addFreshEntity(projectile)
    }

    companion object {
        // The number of ticks inbetween shots (5 seconds)
        private const val TIME_BETWEEN_ATTACKS = 20 * 5
    }
}