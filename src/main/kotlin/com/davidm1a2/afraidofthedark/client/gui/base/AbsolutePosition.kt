package com.davidm1a2.afraidofthedark.client.gui.base

class AbsolutePosition(x: Double, y: Double): Position<Double>(x, y) {
    fun add(other: AbsolutePosition): AbsolutePosition {
        return AbsolutePosition(x + other.x, y + other.y)
    }

    fun sub(other: AbsolutePosition): AbsolutePosition {
        return AbsolutePosition(x - other.x, y - other.y)
    }

    fun avg(other: AbsolutePosition): AbsolutePosition {
        return AbsolutePosition((x + other.x)/2, (y + other.y)/2)
    }

    fun toDimensions(): AbsoluteDimensions {
        return AbsoluteDimensions(x, y)
    }

    fun toRelative(reference: AOTDPane): RelativePosition {
        return RelativePosition(this.x / reference.width, this.y / reference.height)
    }
}