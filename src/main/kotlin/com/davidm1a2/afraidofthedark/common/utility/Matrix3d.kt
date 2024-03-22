package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.world.phys.Vec3

/**
 * Minecraft does not provide a matrix class for us :/ Use our own
 */
class Matrix3d(
    var m00: Double, var m01: Double, var m02: Double,
    var m10: Double, var m11: Double, var m12: Double,
    var m20: Double, var m21: Double, var m22: Double
) {
    constructor() : this(
        0.0, 0.0, 0.0,
        0.0, 0.0, 0.0,
        0.0, 0.0, 0.0
    )

    fun clone(): Matrix3d {
        return Matrix3d(
            m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22
        )
    }

    fun mul(scalar: Double): Matrix3d {
        m00 = m00 * scalar
        m01 = m01 * scalar
        m02 = m02 * scalar

        m10 = m10 * scalar
        m11 = m11 * scalar
        m12 = m12 * scalar

        m20 = m20 * scalar
        m21 = m21 * scalar
        m22 = m22 * scalar

        return this
    }

    fun mul(vec: Vec3): Vec3 {
        return Vec3(
            m00 * vec.x + m01 * vec.y + m02 * vec.z,
            m10 * vec.x + m11 * vec.y + m12 * vec.z,
            m20 * vec.x + m21 * vec.y + m22 * vec.z,
        )
    }

    @Suppress("DUPLICATES")
    fun mul(m1: Matrix3d): Matrix3d {
        val newM00 = this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20
        val newM01 = this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21
        val newM02 = this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22

        val newM10 = this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20
        val newM11 = this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21
        val newM12 = this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22

        val newM20 = this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20
        val newM21 = this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21
        val newM22 = this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22

        this.m00 = newM00
        this.m01 = newM01
        this.m02 = newM02

        this.m10 = newM10
        this.m11 = newM11
        this.m12 = newM12

        this.m20 = newM20
        this.m21 = newM21
        this.m22 = newM22

        return this
    }

    fun add(matrix3d: Matrix3d): Matrix3d {
        m00 = m00 + matrix3d.m00
        m01 = m01 + matrix3d.m01
        m02 = m02 + matrix3d.m02

        m10 = m10 + matrix3d.m10
        m11 = m11 + matrix3d.m11
        m12 = m12 + matrix3d.m12

        m20 = m20 + matrix3d.m20
        m21 = m21 + matrix3d.m21
        m22 = m22 + matrix3d.m22

        return this
    }

    fun setIdentity(): Matrix3d {
        m00 = 1.0
        m01 = 0.0
        m02 = 0.0

        m10 = 0.0
        m11 = 1.0
        m12 = 0.0

        m20 = 0.0
        m21 = 0.0
        m22 = 1.0

        return this
    }
}