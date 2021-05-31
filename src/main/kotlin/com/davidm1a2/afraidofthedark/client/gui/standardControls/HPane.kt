package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.layout.GuiGravity
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeSpacing

class HPane(val layout: Layout = Layout.SPREAD) : StackPane() {

    override fun calcChildrenBounds(width: Double, height: Double) {
        // Calculate padding
        var calcPadding = padding
        if (calcPadding is RelativeSpacing) calcPadding = calcPadding.toAbsolute(this)
        val internalWidth = width - calcPadding.horizPx
        val internalHeight = height - calcPadding.vertPx

        // Calculate an initial space allowance for each child
        val initSpace = internalWidth / getChildren().size
        var extraSpace = 0.0

        // Set initial dimensions
        for (child in getChildren()) {
            // Calculate margins
            var calcMargins = child.margins
            if (calcMargins is RelativeSpacing) calcMargins = calcMargins.toAbsolute(child)
            val marginWidth = calcMargins.horizPx
            val marginHeight = calcMargins.vertPx
            // Set dimensions
            child.negotiateDimensions(internalWidth - marginWidth, internalHeight - marginHeight)
        }
        // Find any extra space
        for (child in getChildren()) {
            // Calculate margins
            var calcMargins = child.margins
            if (calcMargins is RelativeSpacing) calcMargins = calcMargins.toAbsolute(child)
            val marginWidth = calcMargins.horizPx
            // Accumulate extra space
            val childWidthWithMargins = child.width + marginWidth
            if (childWidthWithMargins < initSpace) {
                extraSpace += initSpace - childWidthWithMargins
            }
        }
        // Find children that want extra space
        var requesterCount = 0
        for (child in getChildren()) {
            // Calculate margins
            var calcMargins = child.margins
            if (calcMargins is RelativeSpacing) calcMargins = calcMargins.toAbsolute(child)
            val marginWidth = calcMargins.horizPx
            // Accumulate number of children with space requests
            val childWidthWithMargins = child.width + marginWidth
            if (childWidthWithMargins > initSpace) {
                requesterCount++
            }
        }
        // Allocate extra space
        for (child in getChildren()) {
            // Calculate margins
            var calcMargins = child.margins
            if (calcMargins is RelativeSpacing) calcMargins = calcMargins.toAbsolute(child)
            val marginWidth = calcMargins.horizPx
            // Allocate space evenly
            val childWidthWithMargins = child.width + marginWidth
            if (childWidthWithMargins > initSpace) {
                val extraSpaceAllowed = extraSpace / requesterCount
                val requestedSpace = childWidthWithMargins - initSpace
                if (requestedSpace > extraSpaceAllowed) {   // Request exceeds space allowance
                    child.negotiateDimensions(initSpace + extraSpaceAllowed - marginWidth, internalHeight)
                    extraSpace -= extraSpaceAllowed
                } else {    // Request is within space allowance
                    extraSpace -= requestedSpace
                }
                requesterCount--
            }
        }
        // Determine offsets
        var xOffset = when (layout) {
            Layout.SPREAD -> 0.0
            Layout.SPREAD_INSIDE -> extraSpace / (getChildren().size + 1)
            Layout.CLOSE -> extraSpace / 2.0
        }
        for (child in getChildren()) {
            // Calculate margins
            var calcMargins = child.margins
            if (calcMargins is RelativeSpacing) calcMargins = calcMargins.toAbsolute(child)
            val marginHeight = calcMargins.vertPx
            val marginWidth = calcMargins.horizPx
            // Calculate position
            val gravityXOffset = 0.0    // Gravity doesn't affect the x dimension because cells are fitted to children
            val gravityYOffset = when (child.gravity) {
                GuiGravity.TOP_LEFT, GuiGravity.TOP_CENTER, GuiGravity.TOP_RIGHT -> calcPadding.top
                GuiGravity.CENTER_LEFT, GuiGravity.CENTER, GuiGravity.CENTER_RIGHT -> height/2 - (child.height + marginHeight)/2
                GuiGravity.BOTTOM_LEFT, GuiGravity.BOTTOM_CENTER, GuiGravity.BOTTOM_RIGHT -> height - (child.height + marginHeight) - calcPadding.bot
            }
            // Calculate offset from position in HPane, not according to child.offset
            val yOffset = 0.0
            // Set position
            child.x = (this.x + this.guiOffsetX + gravityXOffset + xOffset + calcMargins.left).toInt()
            child.y = (this.y + this.guiOffsetY + gravityYOffset + yOffset + calcMargins.top).toInt()
            // Calculate offset based on our layout
            val layoutOffset = when(layout) {
                Layout.SPREAD -> if (getChildren().size == 1) 0.0 else extraSpace / (getChildren().size - 1)
                Layout.SPREAD_INSIDE -> extraSpace / (getChildren().size + 1)
                Layout.CLOSE -> 0.0
            }
            // Add to next child's offset
            xOffset += child.width + marginWidth + layoutOffset
            // If it's a pane, have it recalculate its children too
            if (child is AOTDPane) child.calcChildrenBounds()
            // Determine if the subtree of children are in this node's bounds
            determineInBounds(child)
        }
    }

    enum class Layout {
        SPREAD,
        SPREAD_INSIDE,
        CLOSE
    }
}