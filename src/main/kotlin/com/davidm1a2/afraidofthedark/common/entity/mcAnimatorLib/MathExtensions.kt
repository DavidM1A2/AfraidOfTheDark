package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib

import com.mojang.math.Quaternion
import com.mojang.math.Vector3f
import net.minecraft.world.phys.Vec3
import kotlin.math.acos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Slerp sets this quaternion's value as an interpolation between two other quaternions
 *
 * @param q1 the first quaternion
 * @param q2 the second quaternion
 * @param t  the amount to interpolate between the two quaternions
 */
fun Quaternion.slerp(q1: Quaternion, q2: Quaternion, t: Float): Quaternion {
    // Create a local quaternion to store the interpolated quaternion
    if (q1.i() == q2.i() && q1.j() == q2.j() && q1.k() == q2.k() && q1.r() == q2.r()) {
        this.set(q1.i(), q1.j(), q1.k(), q1.r())
        return this
    }

    var result = q1.i() * q2.i() + q1.j() * q2.j() + q1.k() * q2.k() + q1.r() * q2.r()

    if (result < 0.0f) {
        // Negate the second quaternion and the result of the dot product
        q2.set(-q2.i(), -q2.j(), -q2.k(), -q2.r())
        result = -result
    }

    // Set the first and second scale for the interpolation
    var scale0 = 1 - t
    var scale1 = t

    // Check if the angle between the 2 quaternions was big enough to
    // warrant such calculations
    if (1 - result > 0.1f) {
        // Get the angle between the 2 quaternions,
        // and then store the sin() of that angle
        val theta = acos(result)
        val invSinTheta = 1f / sin(theta)

        // Calculate the scale for q1 and q2, according to the angle and
        // it's sine value
        scale0 = sin((1 - t) * theta) * invSinTheta
        scale1 = sin(t * theta) * invSinTheta
    }

    // Calculate the x, y, z and w values for the quaternion by using a
    // special
    // form of linear interpolation for quaternions.

    set(
        scale0 * q1.i() + scale1 * q2.i(),
        scale0 * q1.j() + scale1 * q2.j(),
        scale0 * q1.k() + scale1 * q2.k(),
        scale0 * q1.r() + scale1 * q2.r()
    )

    // Return the interpolated quaternion
    return this
}

fun Vector3f.interpolate(otherVec: Vector3f, percent: Float) {
    this.setX((1 - percent) * this.x() + percent * otherVec.x())
    this.setY((1 - percent) * this.y() + percent * otherVec.y())
    this.setZ((1 - percent) * this.z() + percent * otherVec.z())
}

/**
 * Computes the rotation needed to go from the source vector to the target vector as a Quaternion. For more info see:
 * https://stackoverflow.com/questions/1171849/finding-quaternion-representing-the-rotation-from-one-vector-to-another/1171995#1171995
 */
private val X_UNIT_VECTOR = Vec3(1.0, 0.0, 0.0)
private val Y_UNIT_VECTOR = Vec3(0.0, 1.0, 0.0)
fun Vec3.computeRotationTo(target: Vec3): Quaternion {
    val normalizedBasis = this.normalize()
    val normalizedTarget = target.normalize()
    val angleBetweenVectors = normalizedBasis.dot(normalizedTarget)

    // If the angle is close to -1 it indicates the vectors are at a 180deg angle, eg: <---- and ---->
    // In this case we have to rotate the vector by 180 degrees around either the X or Y axis. The reason we
    // try X OR Y is that the vectors might be parallel to the X or Y axis, so pick the one which isn't parallel
    if (angleBetweenVectors < -0.999999) {
        var orthogonalVec = X_UNIT_VECTOR.cross(normalizedBasis)
        if (orthogonalVec.length() < 0.00001) {
            orthogonalVec = Y_UNIT_VECTOR.cross(normalizedBasis)
        }

        return Quaternion(Vector3f(orthogonalVec.normalize()), 180f, true)
    }

    // If the angle is close to 1 it indicates the vectors are perfectly parallel, eg: ----> and ---->
    // In this case we do no rotations and return the unit quaternion
    if (angleBetweenVectors > 0.999999) {
        return Quaternion.ONE
    }

    // The default case is our vectors are at some angle from one another. Compute the rotation quaternion needed
    val cross = normalizedBasis.cross(target)
    val rotation = Quaternion(
        cross.x.toFloat(),
        cross.y.toFloat(),
        cross.z.toFloat(),
        (sqrt(normalizedBasis.lengthSqr() * normalizedTarget.lengthSqr()) + angleBetweenVectors).toFloat()
    )
    rotation.normalize()
    return rotation
}

// Returns a pair of vectors that are both orthogonal to the original vector. The called on vector must be unit length
fun Vec3.getOrthogonalVectors(): Pair<Vec3, Vec3> {
    if (this == Vec3.ZERO) {
        return this to this
    }

    var leftRightDirection = Y_UNIT_VECTOR.cross(this)
    if (leftRightDirection == Vec3.ZERO) {
        leftRightDirection = X_UNIT_VECTOR
    }
    val upDownDirection = leftRightDirection.cross(this)

    return leftRightDirection to upDownDirection
}