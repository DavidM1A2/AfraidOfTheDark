package com.davidm1a2.afraidofthedark.client.gui.base

open class Dimensions<T:Number>(val width: T, val height: T) {
    override fun equals(other: Any?): Boolean {
        return if (other is Dimensions<*>) other.width == this.width && other.height == this.height else false
    }

    override fun hashCode(): Int {
        var result = width.hashCode()
        result = 31 * result + height.hashCode()
        return result
    }
}