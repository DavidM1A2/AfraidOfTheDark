package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object ConeUtils {
    fun getBoundingBox(
        position: Vec3,
        direction: Vec3,
        radius: Double,
        length: Double,
        normal: Vec3 = Vec3(0.0, 1.0, 0.0)
    ): AABB {
        var upDir = normal
        val downDir = upDir.reverse()
        var leftDir = direction.cross(upDir).normalize()
        if (leftDir == Vec3.ZERO) {
            upDir = Vec3(1.0, 0.0, 0.0)
            leftDir = direction.cross(upDir).normalize()
        }
        val rightDir = leftDir.reverse()

        // Find four corners of the box that bounds the base circle, as well as the tip position (which we already know)
        val baseCenterPos = position.add(direction.scale(length))
        val cornerOne = baseCenterPos.add(leftDir.scale(radius)).add(upDir.scale(radius))
        val cornerTwo = baseCenterPos.add(leftDir.scale(radius)).add(downDir.scale(radius))
        val cornerThree = baseCenterPos.add(rightDir.scale(radius)).add(upDir.scale(radius))
        val cornerFour = baseCenterPos.add(rightDir.scale(radius)).add(downDir.scale(radius))

        // Find the smallest (x, y, z) and biggest (x, y, z) coordinates. Ceil/floor the values, so we don't cut off any blocks partially within the cone
        val minX = minOf(position.x, cornerOne.x, cornerTwo.x, cornerThree.x, cornerFour.x)
        val minY = minOf(position.y, cornerOne.y, cornerTwo.y, cornerThree.y, cornerFour.y)
        val minZ = minOf(position.z, cornerOne.z, cornerTwo.z, cornerThree.z, cornerFour.z)
        val maxX = maxOf(position.x, cornerOne.x, cornerTwo.x, cornerThree.x, cornerFour.x)
        val maxY = maxOf(position.y, cornerOne.y, cornerTwo.y, cornerThree.y, cornerFour.y)
        val maxZ = maxOf(position.z, cornerOne.z, cornerTwo.z, cornerThree.z, cornerFour.z)

        return AABB(minX, minY, minZ, maxX, maxY, maxZ)
    }
}