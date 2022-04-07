package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import net.minecraft.command.arguments.EntityAnchorArgument
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.vector.Vector3d
import java.util.EnumSet
import kotlin.math.cos
import kotlin.math.sin

abstract class FrostPhoenixMoveBaseGoal(protected val phoenix: FrostPhoenixEntity) : Goal() {
    init {
        flags = EnumSet.of(Flag.MOVE, Flag.LOOK)
    }

    protected fun getCurrentTargetPosition(): Vector3d {
        val spawnerPos = phoenix.spawnerPos
        val ticksAlive = phoenix.tickCount
        val x = spawnerPos.x + 0.5 + sin(ticksAlive / 45.0) * FrostPhoenixFlyGoal.FLY_DIAMETER
        val y = spawnerPos.y + 0.5 + MAX_FLY_HEIGHT
        val z = spawnerPos.z + 0.5 + cos(ticksAlive / 45.0) * FrostPhoenixFlyGoal.FLY_DIAMETER
        return Vector3d(x, y, z)
    }

    protected fun flyTo(x: Double, y: Double, z: Double) {
        phoenix.moveControl.setWantedPosition(x, y, z, phoenix.getAttributeValue(Attributes.MOVEMENT_SPEED))
        lookAt(x, y, z)
    }

    protected fun lookAt(x: Double, y: Double, z: Double) {
        phoenix.lookAt(EntityAnchorArgument.Type.EYES, Vector3d(x, y, z))
    }

    companion object {
        internal const val MAX_FLY_HEIGHT = 10.0
    }
}