package com.davidm1a2.afraidofthedark.client.gui.layout

abstract class Position(val x: Double, val y: Double) {
    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Position) other.x == this.x && other.y == this.y else false
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}