package com.davidm1a2.afraidofthedark.client.gui.layout

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane

class RelativeDimensions(width: Double, height: Double): Dimensions(width, height) {
    fun add(other: RelativeDimensions): RelativeDimensions {
        return RelativeDimensions(width + other.width, height + other.height)
    }

    fun sub(other: RelativeDimensions): RelativeDimensions {
        return RelativeDimensions(width - other.width, height - other.height)
    }

    fun avg(other: RelativeDimensions): RelativeDimensions {
        return RelativeDimensions((width + other.width) / 2, (height + other.height) / 2)
    }

    fun toPosition(): RelativePosition {
        return RelativePosition(width, height)
    }

    fun toAbsolute(reference: AOTDPane): AbsolutePosition {
        return AbsolutePosition(this.width * (reference.width - reference.padding.horizPx), this.height * (reference.height - reference.padding.vertPx))
    }
}