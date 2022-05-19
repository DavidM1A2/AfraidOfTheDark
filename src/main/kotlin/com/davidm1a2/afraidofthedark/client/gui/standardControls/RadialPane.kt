package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import kotlin.math.roundToInt

/**
 * Lays out child GUI elements in a radial manner, interpreting (x,y) offsets as (r,theta) coordinates.
 */
open class RadialPane : RatioPane(1, 1) {

    override fun calcChildrenBounds() {// Calculate padding
        val calcPadding = padding.getAbsoluteOuter(this)
        val internalWidth = this.getInternalWidth()
        val internalHeight = this.getInternalHeight()

        for (child in this.getChildren()) {
            // Calculate margins
            val calcMargins = child.margins.getAbsoluteOuter(child)
            val marginWidth = calcMargins.width
            val marginHeight = calcMargins.height
            // Set dimensions
            child.negotiateDimensions(internalWidth - marginWidth, internalHeight - marginHeight)
            // Calculate position
            val gravityXOffset = when (child.gravity) {
                Gravity.TOP_LEFT, Gravity.CENTER_LEFT, Gravity.BOTTOM_LEFT -> calcMargins.left
                Gravity.TOP_CENTER, Gravity.CENTER, Gravity.BOTTOM_CENTER -> internalWidth / 2 - (child.width + calcMargins.width) / 2 + calcMargins.left
                Gravity.TOP_RIGHT, Gravity.CENTER_RIGHT, Gravity.BOTTOM_RIGHT -> internalWidth - child.width - calcMargins.right
            }
            val gravityYOffset = when (child.gravity) {
                Gravity.TOP_LEFT, Gravity.TOP_CENTER, Gravity.TOP_RIGHT -> calcMargins.top
                Gravity.CENTER_LEFT, Gravity.CENTER, Gravity.CENTER_RIGHT -> internalHeight / 2 - (child.height + calcMargins.height) / 2 + calcMargins.top
                Gravity.BOTTOM_LEFT, Gravity.BOTTOM_CENTER, Gravity.BOTTOM_RIGHT -> internalHeight - child.height - calcMargins.bot
            }
            // TODO: Treat child offset as polar coordinates
            val offset = child.offset.getAbsolute(this)
            // Set position
            child.x = (this.x + this.guiOffsetX + calcPadding.left + gravityXOffset + offset.x).roundToInt()
            child.y = (this.y + this.guiOffsetY + calcPadding.top + gravityYOffset + offset.y).roundToInt()
            // If it's a pane, have it recalculate its children too
            if (child is AOTDPane) child.calcChildrenBounds()
            // Determine if the subtree of children are in this node's bounds
            determineInBounds(child)
        }
    }

}