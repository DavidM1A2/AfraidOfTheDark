package com.davidm1a2.afraidofthedark.client.gui.base

class RelativeDimensions(width: Double, height: Double): Dimensions<Double>(width, height) {
    fun add(other: RelativeDimensions): RelativeDimensions {
        return RelativeDimensions(width + other.width, height + other.height)
    }

    fun sub(other: RelativeDimensions): RelativeDimensions {
        return RelativeDimensions(width - other.width, height - other.height)
    }

    fun toPosition(): RelativePosition {
        return RelativePosition(width, height)
    }

    fun toAbsolute(reference: AOTDPane): AbsolutePosition {
        return AbsolutePosition(this.width * reference.width, this.height * reference.height)
    }
}