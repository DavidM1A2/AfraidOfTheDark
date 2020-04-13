package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.vecmath.Matrix4f
import javax.vecmath.Quat4f
import kotlin.math.sqrt

/**
 * This class was provided by the MC animator library and updated to Kotlin
 */
object Utils {
    /**
     * Make a direct NIO FloatBuffer from an array of floats
     *
     * @param arr The array
     * @return The newly created FloatBuffer
     */
    fun makeFloatBuffer(arr: FloatArray): FloatBuffer {
        val bb = ByteBuffer.allocateDirect(arr.size * 4)
        bb.order(ByteOrder.nativeOrder())
        val fb = bb.asFloatBuffer()
        fb.put(arr)
        fb.position(0)
        return fb
    }

    /**
     * Get the Quat4f from a matrix. We need to transpose the matrix.
     *
     * @param matrix The matrics to convert
     * @return A Quat4f representing the 4d matrix
     */
    fun getQuat4fFromMatrix(matrix: Matrix4f): Quat4f {
        val copy = Matrix4f(matrix).transposeAndReturn()

        var x = 0f
        var y = 0f
        var z = 0f
        var w = 0f

        val t = 1.0 + copy.m00 + copy.m11 + copy.m22
        val s: Double
        if (t > 0.00000001) //to avoid large distortions!
        {
            s = sqrt(t) * 2
            x = ((copy.m12 - copy.m21) / s).toFloat()
            y = ((copy.m02 - copy.m20) / s).toFloat()
            z = ((copy.m10 - copy.m01) / s).toFloat()
            w = (0.25 * s).toFloat()
        } else if (t == 0.0) {
            if (copy.m00 > copy.m11 && copy.m00 > copy.m22) { // Column 0:
                s = sqrt(1.0 + copy.m00 - copy.m11 - copy.m22) * 2
                x = (0.25 * s).toFloat()
                y = ((copy.m10 + copy.m01) / s).toFloat()
                z = ((copy.m02 + copy.m20) / s).toFloat()
                w = ((copy.m21 - copy.m12) / s).toFloat()
            } else if (copy.m11 > copy.m22) { // Column 1:
                s = sqrt(1.0 + copy.m11 - copy.m00 - copy.m22) * 2
                x = ((copy.m10 + copy.m01) / s).toFloat()
                y = (0.25 * s).toFloat()
                z = ((copy.m21 + copy.m12) / s).toFloat()
                w = ((copy.m02 - copy.m20) / s).toFloat()
            } else { // Column 2:
                s = sqrt(1.0 + copy.m22 - copy.m00 - copy.m11) * 2
                x = ((copy.m02 + copy.m20) / s).toFloat()
                y = ((copy.m21 + copy.m12) / s).toFloat()
                z = (0.25 * s).toFloat()
                w = ((copy.m10 - copy.m01) / s).toFloat()
            }
        }
        return Quat4f(x, y, z, w)
    }
}