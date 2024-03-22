package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.Util
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

fun Entity.sendMessage(component: Component) {
    sendMessage(component, Util.NIL_UUID)
}

private val LOOK_UP_NORMAL_BASIS = Vec3(0.0, 0.0, -1.0)
private val LOOK_DOWN_NORMAL_BASIS = Vec3(0.0, 0.0, 1.0)
private val UP_BASIS = Vec3(0.0, 1.0, 0.0)
fun Entity.getLookNormal(): Vec3 {
    var normal = lookAngle.getNormal()
    // Edge case when we're looking straight up
    if (normal == Vec3.ZERO) {
        val isLookingDown = lookAngle.y < 0
        // Rotate around an axis based on if we're looking down or up
        val normalBasis = if (isLookingDown) LOOK_DOWN_NORMAL_BASIS else LOOK_UP_NORMAL_BASIS
        normal = normalBasis.rotateAround(UP_BASIS, Math.toRadians(this.yHeadRot.toDouble()))
    }
    return normal
}