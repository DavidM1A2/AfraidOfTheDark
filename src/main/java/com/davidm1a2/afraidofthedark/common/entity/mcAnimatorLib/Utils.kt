package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * This class was provided by the MC animator library
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
     * Get the quaternion from a matrix. We need to transpose the matrix.
     *
     * @param matrix The matrics to convert
     * @return A quaternion representing the 4d matrix
     */
    fun getQuaternionFromMatrix(matrix: Matrix4f): Quaternion {
        val copy = Matrix4f(matrix)
        return Quaternion(copy.transpose())
    }
}