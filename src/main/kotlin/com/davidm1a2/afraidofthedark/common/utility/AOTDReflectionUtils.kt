package com.davidm1a2.afraidofthedark.common.utility

import sun.misc.Unsafe
import java.lang.reflect.Field

/**
 * Herein lies madness. Writing "private static final" by field memory offset feels a bit too C-like, but alas, here we are.
 */
object AOTDReflectionUtils {
    private val unsafe: Unsafe

    init {
        val unsafeField = Unsafe::class.java.getDeclaredField("theUnsafe")
        unsafeField.isAccessible = true
        unsafe = unsafeField.get(null) as Unsafe
    }

    fun setStaticFinalField(field: Field, value: Any?) {
        val staticFieldBase = unsafe.staticFieldBase(field)
        val staticFieldOffset = unsafe.staticFieldOffset(field)

        unsafe.putObject(staticFieldBase, staticFieldOffset, value)
    }
}