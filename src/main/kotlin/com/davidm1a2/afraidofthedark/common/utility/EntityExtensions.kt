package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.entity.Entity
import net.minecraft.util.Util
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.text.ITextComponent

fun Entity.sendMessage(textComponent: ITextComponent) {
    sendMessage(textComponent, Util.NIL_UUID)
}

private val LOOK_UP_NORMAL_BASIS = Vector3d(0.0, 0.0, -1.0)
private val LOOK_DOWN_NORMAL_BASIS = Vector3d(0.0, 0.0, 1.0)
private val UP_BASIS = Vector3d(0.0, 1.0, 0.0)
fun Entity.getLookNormal(): Vector3d {
    var normal = lookAngle.getNormal()
    // Edge case when we're looking straight up
    if (normal == Vector3d.ZERO) {
        val isLookingDown = lookAngle.y < 0
        // Rotate around an axis based on if we're looking down or up
        val normalBasis = if (isLookingDown) LOOK_DOWN_NORMAL_BASIS else LOOK_UP_NORMAL_BASIS
        normal = normalBasis.rotateAround(UP_BASIS, Math.toRadians(entity.yHeadRot.toDouble()))
    }
    return normal
}