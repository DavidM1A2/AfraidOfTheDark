package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.sin

abstract class FrostPhoenixMoveBaseGoal(protected val phoenix: FrostPhoenixEntity) : Goal() {
    private var flyPatternBaseOffsetX = 0.0
    private var flyPatternBaseOffsetZ = 0.0

    init {
        flags = EnumSet.of(Flag.MOVE, Flag.LOOK)
    }

    override fun start() {
        super.start()
        flyPatternBaseOffsetX = phoenix.random.nextDouble() * 2 * Math.PI
        flyPatternBaseOffsetZ = phoenix.random.nextDouble() * 2 * Math.PI
    }

    protected fun getCurrentTargetPosition(): Vec3 {
        val spawnerPos = phoenix.spawnerPos
        val ticksAlive = phoenix.tickCount
        val currentTarget = phoenix.target
        val centerX = currentTarget?.x ?: (spawnerPos.x + 0.5)
        val centerY = currentTarget?.y ?: (spawnerPos.y + 0.5)
        val centerZ = currentTarget?.z ?: (spawnerPos.z + 0.5)

        val currentOffset = ticksAlive / 45.0
        val x = centerX + sin(flyPatternBaseOffsetX + currentOffset) * FrostPhoenixFlyGoal.FLY_DIAMETER
        val y = centerY + MAX_FLY_HEIGHT
        val z = centerZ + sin(2.0 * (flyPatternBaseOffsetZ + currentOffset)) / 2.0 * FrostPhoenixFlyGoal.FLY_DIAMETER
        return Vec3(x, y, z)
    }

    protected fun flyTo(x: Double, y: Double, z: Double) {
        phoenix.moveControl.setWantedPosition(x, y, z, phoenix.getAttributeValue(Attributes.MOVEMENT_SPEED))
        lookAt(x, y, z)
    }

    protected fun lookAt(x: Double, y: Double, z: Double) {
        phoenix.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(x, y, z))
    }

    companion object {
        internal const val MAX_FLY_HEIGHT = 10.0
    }
}