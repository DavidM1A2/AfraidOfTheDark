package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.base.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.base.RelativeDimensions
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import net.minecraft.util.ResourceLocation
import java.awt.Image
import kotlin.math.roundToInt

/**
 * Advanced control that represents a scroll bar which can be dragged up and down
 *
 * @param x                    The scroll bar's x position
 * @param y                    The scroll bar's y position
 * @param width                The scroll bar's width
 * @param height               The scroll bar's height
 * @param scrollBarTexture     The scroll bar background texture
 * @param handleTexture        The scroll bar handle texture
 * @param handleHoveredTexture The scroll bar hovered handle texture
 * @property value The location of the slider, should be between 0 and 1
 * @property handle A reference to the handle object
 * @property handleHeld Flag telling us if the handle is held or not
 * @property originalMousePressLocation An int telling us the screen position the mouse was pressed at
 * @property originalHandleLocation A float telling us where the handle was before the drag began
 */
class AOTDGuiScrollBar @JvmOverloads constructor(
    prefSize: Dimensions<Double>,
    scrollBarTexture: String = "afraidofthedark:textures/gui/scroll_bar.png",
    handleTexture: String = "afraidofthedark:textures/gui/scroll_bar_handle.png",
    handleHoveredTexture: String = handleTexture
) : AOTDPane(prefSize = prefSize) {
    var value = 0.0
        private set
    private val handle: AOTDGuiButton
    private var handleHeld = false
    private var originalMousePressLocation = 0
    private var originalHandleLocation = 0.0

    init {
        // The background behind the scroll bar handle
        val barBackground = ImagePane(ResourceLocation(scrollBarTexture), ImagePane.DispMode.FIT_TO_PARENT)
        // Add the background to the control
        this.add(barBackground)
        // Create a handle to grab, let the height be the height of the bar / 10
        val icon = ImagePane(ResourceLocation(handleTexture), ImagePane.DispMode.STRETCH)
        val iconHovered = ImagePane(ResourceLocation(handleHoveredTexture), ImagePane.DispMode.STRETCH)
        this.handle = AOTDGuiButton(RelativeDimensions(1.0, 0.1), icon = icon, iconHovered = iconHovered)
        // Add the handle
        this.add(this.handle)

        // When we click the mouse we update the state of the handle to being held/released
        this.handle.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                // If we clicked the lmb set its state to hovered
                if (it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    // We're holding the handle
                    handleHeld = true
                    // Store the handle's current pos
                    originalMousePressLocation = it.mouseY
                    originalHandleLocation = value
                }
            } else if (it.eventType == AOTDMouseEvent.EventType.Release) {
                // Ensure the lmb was released
                if (it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    // No longer holding the mouse down, the handle isn't held anymore
                    handleHeld = false
                }
            }
        }

        // Add a listener to the handle that moves it when lmb is down
        this.handle.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Move) {
                // If we're holding the handle move it
                if (handleHeld) {
                    // Compute the offset between mouse and original hold location
                    val yOffset = it.mouseY - originalMousePressLocation
                    // Move the the handle based on the offset
                    moveHandle(yOffset, false)
                }
            }
        }
    }

    /**
     * Moves the handle up (+) or down (-) by yAmount
     *
     * @param yAmount    The amount to move the handle
     * @param isRelative True if the yAmount is relative to the current position, false if not
     */
    fun moveHandle(yAmount: Int, isRelative: Boolean) {
        // The minimum y value the handle can have
        val minY = y.toDouble()
        // Compute the difference between the max and min y values
        val maxYDiff = height - handle.height
        // The maximum y value the handle can have
        val maxY = y.toDouble() + maxYDiff

        // Compute where the handle should be based on the y offset, ensure to y offset is scaled to the
        // current y scale
        var newY = (yAmount) + (minY + (if (isRelative) value else originalHandleLocation) * maxYDiff)

        // Clamp the y inside the bar so you can't drag it off the top or bottom
        newY = newY.coerceIn(minY, maxY)
        // Update the y pos of the handle
        handle.y = newY.roundToInt()
        // Set the handle location to be the percent down the bar the handle is
        value = (newY - minY) / maxYDiff
    }
}