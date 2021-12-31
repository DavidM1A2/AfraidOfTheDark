package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.util.math.vector.Vector3d
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

fun Vector3d.rotateAround(axis: Vector3d, radians: Double): Vector3d {
    // Use the Rodrigues formula to construct a rotation matrix around "axis" "radians" amount
    // https://math.stackexchange.com/questions/2741515/rotation-around-a-vector
    val basisMatrix = Matrix3d(
        0.0, -axis.z, axis.y,
        axis.z, 0.0, -axis.x,
        -axis.y, axis.x, 0.0
    )

    // Make sure to negate radians, otherwise it's a counter-clockwise rotation
    val termTwo = basisMatrix.clone()
        .mul(sin(-radians))
    val termThree = basisMatrix.clone()
        .mul(basisMatrix.clone())
        .mul(1 - cos(-radians))
    val rotationMatrix = Matrix3d()
        .setIdentity()
        .add(termTwo)
        .add(termThree)

    return rotationMatrix.mul(this)
}

fun Vector3d.getNormal(upBasis: Vector3d = Vector3d(0.0, 1.0, 0.0)): Vector3d {
    val leftRightDir = this.cross(upBasis).normalize()
    // Edge case when the vector we're getting the normal for is 0, 1, 0
    if (leftRightDir == Vector3d.ZERO) {
        return Vector3d.ZERO
    }
    return leftRightDir.cross(this).normalize()
}

fun Double.round(decimalPlaces: Int): Double {
    if (decimalPlaces < 0) {
        return this
    }
    val power = 10.0.pow(decimalPlaces)
    return (this * power).toLong() / power
}

fun Float.round(decimalPlaces: Int): Float {
    if (decimalPlaces < 0) {
        return this
    }
    val power = 10f.pow(decimalPlaces)
    return (this * power).toInt() / power
}