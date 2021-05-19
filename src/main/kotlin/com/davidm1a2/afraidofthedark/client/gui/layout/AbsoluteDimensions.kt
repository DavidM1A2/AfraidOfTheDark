package com.davidm1a2.afraidofthedark.client.gui.layout

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane

class AbsoluteDimensions(width: Double, height: Double): Dimensions(width, height) {
    fun add(other: AbsoluteDimensions): AbsoluteDimensions {
        return AbsoluteDimensions(width + other.width, height + other.height)
    }

    fun sub(other: AbsoluteDimensions): AbsoluteDimensions {
        return AbsoluteDimensions(width - other.width, height - other.height)
    }

    fun avg(other: AbsoluteDimensions): AbsoluteDimensions {
        return AbsoluteDimensions((width + other.width) / 2, (height + other.height) / 2)
    }

    fun toPosition(): AbsolutePosition {
        return AbsolutePosition(width, height)
    }

    fun toRelative(reference: AOTDPane): RelativeDimensions {
        return RelativeDimensions(this.width / (reference.width - reference.padding.horizPx), this.height / (reference.height - reference.padding.vertPx))
    }
}