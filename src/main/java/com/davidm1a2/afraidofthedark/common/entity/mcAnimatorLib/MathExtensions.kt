package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib

import javax.vecmath.Matrix4f
import javax.vecmath.Quat4f
import kotlin.math.acos
import kotlin.math.sin

/**
 * Utility function for the Matrix4f class to set the value and return the matrix
 *
 * @param quat4f The quat to set
 * @return this
 */
fun Matrix4f.setAndReturn(quat4f: Quat4f): Matrix4f {
    set(quat4f)
    return this
}

/**
 * Utility function for the Matrix4f class to transpose and return the matrix
 *
 * @return this
 */
fun Matrix4f.transposeAndReturn(): Matrix4f {
    transpose()
    return this
}

/**
 * Slerp sets this quaternion's value as an interpolation between two other quaternions
 *
 * @param q1 the first quaternion
 * @param q2 the second quaternion
 * @param t  the amount to interpolate between the two quaternions
 */
fun Quat4f.slerp(q1: Quat4f, q2: Quat4f, t: Float): Quat4f {
    // Create a local quaternion to store the interpolated quaternion
    if (q1.x == q2.x && q1.y == q2.y && q1.z == q2.z && q1.w == q2.w) {
        this.set(q1)
        return this
    }

    var result = q1.x * q2.x + q1.y * q2.y + q1.z * q2.z + q1.w * q2.w

    if (result < 0.0f) {
        // Negate the second quaternion and the result of the dot product
        q2.x = -q2.x
        q2.y = -q2.y
        q2.z = -q2.z
        q2.w = -q2.w
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

    x = scale0 * q1.x + scale1 * q2.x
    y = scale0 * q1.y + scale1 * q2.y
    z = scale0 * q1.z + scale1 * q2.z
    w = scale0 * q1.w + scale1 * q2.w

    // Return the interpolated quaternion
    return this
}

/**
 * @return a float array of matrix elements
 */
fun Matrix4f.intoArray(): FloatArray {
    return floatArrayOf(
        m00, m01, m02, m03,
        m10, m11, m12, m13,
        m20, m21, m22, m23,
        m30, m31, m32, m33
    )
}

/**
 * @return if this rotation matrix is rotating about 0 degrees (be sure this is a rotationMatrix!)
 */
fun Matrix4f.isEmptyRotationMatrix(): Boolean {
    if (m00 == 1f && m11 == 1f && m22 == 1f) {
        val m = intoArray()
        var isEmptyRotationMatrix = true
        for (i in m.indices) {
            if (i != 0 && i != 5 && i != 10 && i <= 10) {
                if (m[i] != 0f) {
                    isEmptyRotationMatrix = false
                    break
                }
            }
        }
        return isEmptyRotationMatrix
    }

    return false
}