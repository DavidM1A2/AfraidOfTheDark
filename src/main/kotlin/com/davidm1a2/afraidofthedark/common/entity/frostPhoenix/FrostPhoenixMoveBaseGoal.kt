package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import net.minecraft.command.arguments.EntityAnchorArgument
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.vector.Vector3d
import java.util.EnumSet

abstract class FrostPhoenixMoveBaseGoal(protected val phoenix: FrostPhoenixEntity) : Goal() {
    init {
        flags = EnumSet.of(Flag.MOVE)
    }

    protected fun flyTo(x: Double, y: Double, z: Double) {
        phoenix.moveControl.setWantedPosition(x, y, z, phoenix.getAttributeValue(Attributes.MOVEMENT_SPEED))
        phoenix.lookAt(EntityAnchorArgument.Type.EYES, Vector3d(x, y, z))
    }

    companion object {
        internal const val MAX_FLY_HEIGHT = 10.0
    }
}