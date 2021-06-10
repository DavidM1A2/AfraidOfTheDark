package com.davidm1a2.afraidofthedark.client.gui.layout

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane

class AbsolutePosition(x: Double, y: Double): Position(x, y) {
    fun add(other: AbsolutePosition): AbsolutePosition {
        return AbsolutePosition(x + other.x, y + other.y)
    }

    fun sub(other: AbsolutePosition): AbsolutePosition {
        return AbsolutePosition(x - other.x, y - other.y)
    }

    fun avg(other: AbsolutePosition): AbsolutePosition {
        return AbsolutePosition((x + other.x) / 2, (y + other.y) / 2)
    }

    fun toDimensions(): AbsoluteDimensions {
        return AbsoluteDimensions(x, y)
    }

    fun toRelative(reference: AOTDPane): RelativePosition {
        return RelativePosition(this.x / reference.getInternalWidth(), this.y / reference.getInternalHeight())
    }
}