package com.davidm1a2.afraidofthedark.client.gui.layout

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane

/**
 * Stores a position on the screen
 * A relative position is just a multiplier used on the parent's dimensions to determine a position
 */
class Position(val x: Double = 0.0, val y: Double = 0.0, val isRelative: Boolean = true) {
    fun getAbsolute(reference: AOTDPane) : Position {
        return if (isRelative) {
            Position(x * reference.getInternalWidth(), y * reference.getInternalHeight(), false)
        } else {
            Position(x, y, false)
        }
    }

    fun getRelative(reference: AOTDPane) : Position {
        return if (isRelative) {
            Position(x, y, true)
        } else {
            Position(x / reference.getInternalWidth(), y / reference.getInternalHeight(), true)
        }
    }

    fun toDimension(secondPoint: Position) : Dimensions {
        assert(this.isRelative == secondPoint.isRelative)
        return Dimensions(secondPoint.x - this.x, secondPoint.y - this.y)
    }

    fun avg(secondPoint: Position) : Position {
        assert(this.isRelative == secondPoint.isRelative)
        return Position((this.x + secondPoint.x)/2, (this.y + secondPoint.y)/2)
    }

    override fun equals(other: Any?): Boolean {
        return other is Position && other.isRelative == this.isRelative && other.x == this.x && other.y == other.y
    }
}