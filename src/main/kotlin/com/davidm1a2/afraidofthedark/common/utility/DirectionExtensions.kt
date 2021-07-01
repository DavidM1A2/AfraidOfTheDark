package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.util.Direction
import net.minecraft.util.Rotation

fun Direction.toRotation(): Rotation {
    return when (this) {
        Direction.NORTH -> Rotation.NONE
        Direction.EAST -> Rotation.CLOCKWISE_90
        Direction.SOUTH -> Rotation.CLOCKWISE_180
        Direction.WEST -> Rotation.COUNTERCLOCKWISE_90
        else -> throw IllegalStateException("Direction $this is not a valid rotation")
    }
}