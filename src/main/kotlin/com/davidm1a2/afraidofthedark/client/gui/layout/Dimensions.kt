package com.davidm1a2.afraidofthedark.client.gui.layout

abstract class Dimensions(val width: Double, val height: Double) {
    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Dimensions) other.width == this.width && other.height == this.height else false
    }

    override fun hashCode(): Int {
        var result = width.hashCode()
        result = 31 * result + height.hashCode()
        return result
    }
}