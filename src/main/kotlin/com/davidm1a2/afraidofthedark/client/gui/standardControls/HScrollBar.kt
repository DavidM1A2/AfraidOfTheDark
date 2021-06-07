package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeDimensions
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.GuiGravity
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativePosition
import net.minecraft.util.ResourceLocation
import java.util.function.Consumer

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
class HScrollBar @JvmOverloads constructor(
        prefSize: Dimensions,
        scrollBarTexture: String = "afraidofthedark:textures/gui/scroll_bar.png",
        handleTexture: String = "afraidofthedark:textures/gui/scroll_bar_handle.png",
        handleHoveredTexture: String = handleTexture
) : ImagePane(ResourceLocation(scrollBarTexture), DispMode.FIT_TO_PARENT) {
    var value = 0.0
        set(value) {
            field = value.coerceIn(0.0, 1.0)
            this.handle.offset = RelativePosition(0.0, (field - 0.5) * (0.9))
            this.invalidate()
        }
    private val handle: Button
    private var handleHeld = false
    private var originalMousePressLocation = 0
    private var originalValue = 0.0
    var onValueChanged: Consumer<Double> = Consumer {}

    init {
        // The background behind the scroll bar handle
        this.prefSize = prefSize
        // Create a handle to grab, let the height be the height of the bar / 10
        val icon = ImagePane(ResourceLocation(handleTexture), DispMode.STRETCH)
        val iconHovered = ImagePane(ResourceLocation(handleHoveredTexture), DispMode.STRETCH)
        this.handle = Button(
            icon = icon,
            iconHovered = iconHovered,
            gravity = GuiGravity.CENTER,
            prefSize = RelativeDimensions(1.0, 0.1),
            silent = true
        )
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
                    originalValue = value
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
                    value = originalValue + yOffset / height.toDouble() / 0.9
                    // Call the callback
                    onValueChanged.accept(value)
                }
            }
        }

        value = 0.0
    }
}