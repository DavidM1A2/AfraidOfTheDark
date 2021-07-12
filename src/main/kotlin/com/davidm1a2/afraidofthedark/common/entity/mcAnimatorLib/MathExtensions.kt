package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib

import net.minecraft.client.renderer.Quaternion
import kotlin.math.acos
import kotlin.math.sin

/**
 * Slerp sets this quaternion's value as an interpolation between two other quaternions
 *
 * @param q1 the first quaternion
 * @param q2 the second quaternion
 * @param t  the amount to interpolate between the two quaternions
 */
fun Quaternion.slerp(q1: Quaternion, q2: Quaternion, t: Float): Quaternion {
    // Create a local quaternion to store the interpolated quaternion
    if (q1.x == q2.x && q1.y == q2.y && q1.z == q2.z && q1.w == q2.w) {
        this.set(q1.x, q1.y, q1.z, q1.w)
        return this
    }

    var result = q1.x * q2.x + q1.y * q2.y + q1.z * q2.z + q1.w * q2.w

    if (result < 0.0f) {
        // Negate the second quaternion and the result of the dot product
        q2.set(-q2.x, -q2.y, -q2.z, -q2.w)
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
        scale0 * q1.x + scale1 * q2.x,
        scale0 * q1.y + scale1 * q2.y,
        scale0 * q1.z + scale1 * q2.z,
        scale0 * q1.w + scale1 * q2.w
    )

    // Return the interpolated quaternion
    return this
}