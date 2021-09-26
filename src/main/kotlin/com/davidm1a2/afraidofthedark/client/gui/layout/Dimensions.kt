package com.davidm1a2.afraidofthedark.client.gui.layout

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane

class Dimensions(val width: Double = 0.0, val height: Double = 0.0, val isRelative: Boolean = true) {
    fun getAbsoluteInner(reference: AOTDPane): Dimensions {
        return if (isRelative) {
            Dimensions(width * reference.getInternalWidth(), height * reference.getInternalHeight())
        } else {
            Dimensions(width, height, false)
        }
    }

    fun getAbsoluteOuter(reference: AOTDPane): Dimensions {
        return if (isRelative) {
            Dimensions(width * reference.width, height * reference.height)
        } else {
            Dimensions(width, height, false)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Dimensions && other.isRelative == this.isRelative && other.width == this.width && other.height == other.height
    }
}
