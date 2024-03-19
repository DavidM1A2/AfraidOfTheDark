package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import java.util.*

class FrostPhoenixHurtByPlayerTargetGoal(private val frostPhoenix: FrostPhoenixEntity) : TargetGoal(frostPhoenix, false) {
    private var lastRunTime = 0

    init {
        flags = EnumSet.of(Flag.TARGET)
    }

    override fun canUse(): Boolean {
        if (frostPhoenix.target != null) {
            return true
        }
        val lastHurtTime = mob.lastHurtByMobTimestamp
        val lastHurtMob = mob.lastHurtByMob
        return if (lastHurtTime != this.lastRunTime && lastHurtMob != null) {
            if (lastHurtMob is Player) {
                canAttack(lastHurtMob, HURT_BY_TARGETING)
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun canContinueToUse(): Boolean {
        val phoenixHomePos = frostPhoenix.spawnerPos
        val distanceToSpawnerSquared = frostPhoenix.distanceToSqr(phoenixHomePos.x.toDouble(), phoenixHomePos.y.toDouble(), phoenixHomePos.z.toDouble())
        if (distanceToSpawnerSquared > MAX_DISTANCE_FROM_SPAWNER * MAX_DISTANCE_FROM_SPAWNER) {
            return false
        }
        return super.canContinueToUse()
    }

    override fun start() {
        super.start()
        val newTargetMob = mob.lastHurtByMob
        mob.target = newTargetMob
        targetMob = newTargetMob
        lastRunTime = mob.lastHurtByMobTimestamp
    }

    companion object {
        private val HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting()

        // Can fly up to 100 blocks away
        private const val MAX_DISTANCE_FROM_SPAWNER = 100
    }
}