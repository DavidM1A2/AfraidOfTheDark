package com.davidm1a2.afraidofthedark.client.gui.base

class RelativePosition(x: Double, y: Double): Position<Double>(x, y) {
    fun add(other: RelativePosition): RelativePosition {
        return RelativePosition(x + other.x, y + other.y)
    }

    fun sub(other: RelativePosition): RelativePosition {
        return RelativePosition(x - other.x, y - other.y)
    }

    fun avg(other: RelativePosition): RelativePosition {
        return RelativePosition((x + other.x)/2, (y + other.y)/2)
    }

    fun toDimensions(): RelativeDimensions {
        return RelativeDimensions(x, y)
    }

    fun toAbsolute(reference: AOTDPane): AbsolutePosition {
        return AbsolutePosition(this.x * reference.width, this.y * reference.height)
    }
}