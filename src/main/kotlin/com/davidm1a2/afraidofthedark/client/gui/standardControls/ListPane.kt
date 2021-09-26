package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import java.util.function.Consumer
import kotlin.math.max

/**
 * A subclass of ScrollPane that only scrolls in one dimension, can be used with a scroll bar, and expands to fit children
 */
open class ListPane(val expandDirection: ExpandDirection, val scrollBar: VScrollBar? = null, val scrollSpeed: Double = 10.0) : ScrollPane(1.0, 1.0) {
    private var maxOffset = 0.0

    init {
        scrollBar?.onValueChanged = Consumer {
            when (expandDirection) {
                ExpandDirection.UP -> guiOffsetY = maxOffset * it
                ExpandDirection.DOWN -> guiOffsetY = -maxOffset * it
                ExpandDirection.LEFT -> guiOffsetX = -maxOffset * it
                ExpandDirection.RIGHT -> guiOffsetX = maxOffset * it
            }
            this.invalidate()
        }
        this.addMouseDragListener {
            if (scrollBar != null && maxOffset != 0.0) {
                scrollBar.value = when (expandDirection) {
                    ExpandDirection.UP -> if (maxOffset == 0.0) 0.0 else guiOffsetY / maxOffset
                    ExpandDirection.DOWN -> if (maxOffset == 0.0) 0.0 else guiOffsetY / -maxOffset
                    ExpandDirection.LEFT -> if (maxOffset == 0.0) 0.0 else guiOffsetX / -maxOffset
                    ExpandDirection.RIGHT -> if (maxOffset == 0.0) 0.0 else guiOffsetX / maxOffset
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
                        ExpandDirection.UP -> guiOffsetY += -it.scrollDistance * scrollSpeed
                        ExpandDirection.DOWN -> guiOffsetY += it.scrollDistance * scrollSpeed
                        ExpandDirection.LEFT -> guiOffsetX += it.scrollDistance * scrollSpeed
                        ExpandDirection.RIGHT -> guiOffsetX += -it.scrollDistance * scrollSpeed
                    }
                    checkOutOfBounds()
                    scrollBar?.value = when (expandDirection) {
                        ExpandDirection.UP -> if (maxOffset == 0.0) 0.0 else guiOffsetY / maxOffset
                        ExpandDirection.DOWN -> if (maxOffset == 0.0) 0.0 else guiOffsetY / -maxOffset
                        ExpandDirection.LEFT -> if (maxOffset == 0.0) 0.0 else guiOffsetX / -maxOffset
                        ExpandDirection.RIGHT -> if (maxOffset == 0.0) 0.0 else guiOffsetX / maxOffset
                    }
                    this.invalidate()
                }
            }
        }
    }

    private fun calculateMaxOffset() {
        var tempMaxOffset = 0.0
        val calcPadding = this.padding.getAbsoluteInner(this)
        when (expandDirection) {
            ExpandDirection.UP, ExpandDirection.DOWN -> {
                tempMaxOffset += calcPadding.height
                for (child in getChildren()) {
                    val calcMargins = child.margins.getAbsoluteOuter(child)
                    tempMaxOffset += child.height + calcMargins.height
                }
                tempMaxOffset = max(tempMaxOffset - height, 0.0)
            }
            ExpandDirection.LEFT, ExpandDirection.RIGHT -> {
                tempMaxOffset += calcPadding.width
                for (child in getChildren()) {
                    val calcMargins = child.margins.getAbsoluteOuter(child)
                    tempMaxOffset += child.width + calcMargins.width
                }
                tempMaxOffset = max(tempMaxOffset - width, 0.0)
            }
        }
        maxOffset = tempMaxOffset
    }
    
    private fun recalculateChildrenOffsets() {
        var nextChildOffset = 0.0
        for (child in getChildren()) {
            val calcMargins = child.margins.getAbsoluteOuter(child)
            when (expandDirection) {
                ExpandDirection.UP -> {
                    child.offset = Position(0.0, -nextChildOffset, false).getRelative(this)
                    child.gravity = Gravity.BOTTOM_CENTER
                    nextChildOffset += child.height + calcMargins.height
                }
                ExpandDirection.DOWN -> {
                    child.offset = Position(0.0, nextChildOffset, false).getRelative(this)
                    child.gravity = Gravity.TOP_CENTER
                    nextChildOffset += child.height + calcMargins.height
                }
                ExpandDirection.LEFT -> {
                    child.offset = Position(-nextChildOffset, 0.0, false).getRelative(this)
                    child.gravity = Gravity.CENTER_RIGHT
                    nextChildOffset += child.width + calcMargins.width
                }
                ExpandDirection.RIGHT -> {
                    child.offset = Position(nextChildOffset, 0.0, false).getRelative(this)
                    child.gravity = Gravity.CENTER_LEFT
                    nextChildOffset += child.width + calcMargins.width
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

    override fun calcChildrenBounds() {
        recalculateChildrenOffsets()
        calculateMaxOffset()
        checkOutOfBounds()
        super.calcChildrenBounds()
    }

    enum class ExpandDirection {
        UP, DOWN, LEFT, RIGHT
    }
}