package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.layout.AbsolutePosition
import com.davidm1a2.afraidofthedark.client.gui.layout.GuiGravity
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeSpacing
import java.util.function.Consumer
import kotlin.math.max

class ListPane(val expandDirection: ExpandDirection, val scrollBar: HScrollBar? = null) : ScrollPane(1.0, 1.0) {
    private var maxOffset = 0.0

    init {
        scrollBar?.onValueChanged = Consumer {
            when (expandDirection) {
                ExpandDirection.UP -> guiOffsetY = maxOffset * it
                ExpandDirection.DOWN -> guiOffsetY = -maxOffset * it
                ExpandDirection.LEFT -> guiOffsetX = -maxOffset * it
                ExpandDirection.RIGHT -> guiOffsetX = maxOffset * it
            }
        }
        this.addMouseDragListener {
            if (scrollBar != null && maxOffset != 0.0) {
                scrollBar.value = when (expandDirection) {
                    ExpandDirection.UP -> guiOffsetY / maxOffset
                    ExpandDirection.DOWN -> guiOffsetY / -maxOffset
                    ExpandDirection.LEFT -> guiOffsetX / -maxOffset
                    ExpandDirection.RIGHT -> guiOffsetX / maxOffset
                }
            }
        }
        // When we scroll we want to move the content pane up or down
        this.addMouseScrollListener {
            // Only scroll the pane if it's hovered
            if (this.isHovered) {
                // Only move the handle if scrollDistance is non-zero
                if (it.scrollDistance != 0) {
                    // Move the panel by the distance amount
                    when (expandDirection) {
                        ExpandDirection.UP -> guiOffsetY += maxOffset * -it.scrollDistance * SCROLL_SPEED
                        ExpandDirection.DOWN -> guiOffsetY += -maxOffset * -it.scrollDistance * SCROLL_SPEED
                        ExpandDirection.LEFT -> guiOffsetX += -maxOffset * -it.scrollDistance * SCROLL_SPEED
                        ExpandDirection.RIGHT -> guiOffsetX += maxOffset * -it.scrollDistance * SCROLL_SPEED
                    }
                    checkOutOfBounds()
                    scrollBar?.value = when (expandDirection) {
                        ExpandDirection.UP -> if (maxOffset == 0.0) 0.0 else guiOffsetY / maxOffset
                        ExpandDirection.DOWN -> if (maxOffset == 0.0) 0.0 else guiOffsetY / -maxOffset
                        ExpandDirection.LEFT -> if (maxOffset == 0.0) 0.0 else guiOffsetX / -maxOffset
                        ExpandDirection.RIGHT -> if (maxOffset == 0.0) 0.0 else guiOffsetX / maxOffset
                    }
                }
            }
        }
    }

    private fun calculateMaxOffset() {
        maxOffset = 0.0
        var calcPadding = this.padding
        if (calcPadding is RelativeSpacing) calcPadding = calcPadding.toAbsolute(this)
        when (expandDirection) {
            ExpandDirection.UP, ExpandDirection.DOWN -> {
                maxOffset += calcPadding.vertPx
                for (child in getChildren()) {
                    maxOffset += child.height + child.margins.vertPx
                }
                maxOffset = max(maxOffset - height, 0.0)
            }
            ExpandDirection.LEFT, ExpandDirection.RIGHT -> {
                maxOffset += calcPadding.horizPx
                for (child in getChildren()) {
                    maxOffset += child.width + child.margins.horizPx
                }
                maxOffset = max(maxOffset - width, 0.0)
            }
        }
    }
    
    private fun recalculateChildrenOffsets() {
        var nextChildOffset = 0.0
        for (child in getChildren()) {
            when (expandDirection) {
                ExpandDirection.UP -> {
                    child.offset = AbsolutePosition(0.0, -nextChildOffset).toRelative(this)
                    child.gravity = GuiGravity.BOTTOM_CENTER
                    nextChildOffset += child.height + child.margins.vertPx
                }
                ExpandDirection.DOWN -> {
                    child.offset = AbsolutePosition(0.0, nextChildOffset).toRelative(this)
                    child.gravity = GuiGravity.TOP_CENTER
                    nextChildOffset += child.height + child.margins.vertPx
                }
                ExpandDirection.LEFT -> {
                    child.offset = AbsolutePosition(-nextChildOffset, 0.0).toRelative(this)
                    child.gravity = GuiGravity.CENTER_RIGHT
                    nextChildOffset += child.width + child.margins.horizPx
                }
                ExpandDirection.RIGHT -> {
                    child.offset = AbsolutePosition(nextChildOffset, 0.0).toRelative(this)
                    child.gravity = GuiGravity.CENTER_LEFT
                    nextChildOffset += child.width + child.margins.horizPx
                }
            }
        }
    }

    override fun checkOutOfBounds() {
        val leftBounds = if (expandDirection == ExpandDirection.LEFT) maxOffset else 0.0
        val topBounds = if (expandDirection == ExpandDirection.UP) maxOffset else 0.0
        val rightBounds = if (expandDirection == ExpandDirection.RIGHT) -maxOffset else 0.0
        val botBounds = if (expandDirection == ExpandDirection.DOWN) -maxOffset else 0.0
        guiOffsetX = guiOffsetX.coerceIn(rightBounds, leftBounds)
        guiOffsetY = guiOffsetY.coerceIn(botBounds, topBounds)
    }

    override fun calcChildrenBounds(width: Double, height: Double) {
        recalculateChildrenOffsets()
        calculateMaxOffset()
        super.calcChildrenBounds(width, height)
        checkOutOfBounds()
    }

    enum class ExpandDirection {
        UP, DOWN, LEFT, RIGHT
    }

    companion object {
        const val SCROLL_SPEED = 0.1
    }
}