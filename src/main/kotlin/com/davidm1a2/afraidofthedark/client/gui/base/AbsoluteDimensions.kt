package com.davidm1a2.afraidofthedark.client.gui.base

class AbsoluteDimensions(width: Double, height: Double): Dimensions<Double>(width, height) {
    fun add(other: AbsoluteDimensions): AbsoluteDimensions {
        return AbsoluteDimensions(width + other.width, height + other.height)
    }

    fun sub(other: AbsoluteDimensions): AbsoluteDimensions {
        return AbsoluteDimensions(width - other.width, height - other.height)
    }

    fun toPosition(): AbsolutePosition {
        return AbsolutePosition(width, height)
    }

    fun toRelative(reference: AOTDPane): RelativePosition {
        return RelativePosition(this.width / reference.width, this.height / reference.height)
    }
}