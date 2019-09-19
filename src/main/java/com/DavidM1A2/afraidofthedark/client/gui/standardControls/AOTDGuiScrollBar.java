package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;
import net.minecraft.util.math.MathHelper;

/**
 * Advanced control that represents a scroll bar which can be dragged up and down
 */
public class AOTDGuiScrollBar extends AOTDGuiContainer
{
    // The location of the slider, should be between 0 and 1
    private float handleLocation = 0.0f;
    // Flag telling us if the handle is held or not
    private boolean handleHeld = false;
    // An int telling us the screen position the mouse was pressed at
    private int originalMousePressLocation = 0;
    // A float telling us where the handle was before the drag began
    private float originalHandleLocation = 0;

    /**
     * Constructor initializes the position of the scroll bar and its width/height
     *
     * @param x The scroll bar's x position
     * @param y The scroll bar's y position
     * @param width The scroll bar's width
     * @param height The scroll bar's height
     */
    public AOTDGuiScrollBar(int x, int y, int width, int height)
    {
        // Call our other constructor with default textures
        this(x, y, width, height, "afraidofthedark:textures/gui/scroll_bar.png", "afraidofthedark:textures/gui/scroll_bar_handle.png");
    }

    /**
     * Constructor initializes the scroll bar given a position, height, and texture
     *
     * @param x The scroll bar's x position
     * @param y The scroll bar's y position
     * @param width The scroll bar's width
     * @param height The scroll bar's height
     * @param scrollBarTexture The scroll bar background texture
     * @param handleTexture The scrol bar handle texture
     */
    public AOTDGuiScrollBar(int x, int y, int width, int height, String scrollBarTexture, String handleTexture)
    {
        this(x, y, width, height, scrollBarTexture, handleTexture, handleTexture);
    }

    /**
     * Constructor initializes the scroll bar given a position, height, and texture
     *
     * @param x The scroll bar's x position
     * @param y The scroll bar's y position
     * @param width The scroll bar's width
     * @param height The scroll bar's height
     * @param scrollBarTexture The scroll bar background texture
     * @param handleTexture The scroll bar handle texture
     * @param handleHoveredTexture The scroll bar hovered handle texture
     */
    public AOTDGuiScrollBar(int x, int y, int width, int height, String scrollBarTexture, String handleTexture, String handleHoveredTexture)
    {

        super(x, y, width, height);

        // The background behind the scroll bar handle
        AOTDGuiImage barBackground = new AOTDGuiImage(0, 0, width, height, scrollBarTexture);
        // Add the background to the control
        this.add(barBackground);
        // Create a handle to grab, let the height be the height of the bar / 10
        AOTDGuiButton handle = new AOTDGuiButton(0, 0, width, height / 10, null, handleTexture, handleHoveredTexture);
        // Add the handle
        this.add(handle);

        // When we click the mouse we update the state of the handle to being held/released
        handle.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Click)
            {
                // If we clicked the lmb set its state to hovered
                if (event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    // We're holding the handle
                    handleHeld = true;
                    // Store the handle's current pos
                    originalMousePressLocation = event.getMouseY();
                    originalHandleLocation = handleLocation;
                }
            }
            else if (event.getEventType() == AOTDMouseEvent.EventType.Release)
            {
                // Ensure the lmb was released
                if (event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    // No longer holding the mouse down, the handle isn't held anymore
                    handleHeld = false;
                }
            }
        });

        // Add a listener to the handle that moves it when lmb is down
        handle.addMouseMoveListener(event ->
        {
            if (event.getEventType() == AOTDMouseMoveEvent.EventType.Drag)
            {
                // If we're holding the handle move it
                if (handleHeld)
                {
                    // The minimum y value the handle can have
                    int minY = getY();
                    // Compute the difference between the max and min y values
                    int maxYDiff = getHeight() - handle.getHeight();
                    // The maximum y value the handle can have
                    int maxY = getY() + maxYDiff;

                    // Compute the offset between mouse and original hold location
                    int yOffset = event.getMouseY() - originalMousePressLocation;
                    // Compute where the handle should be based on the y offset, ensure to y offset is scaled to the
                    // current y scale
                    int newY = (int) (yOffset / getScaleY()) + (int) (minY + originalHandleLocation * maxYDiff);
                    // Clamp the y inside the bar so you can't drag it off the top or bottom
                    newY = MathHelper.clamp(newY, minY, maxY);
                    // Update the y pos of the handle
                    handle.setY(newY);
                    // Set the handle location to be the percent down the bar the handle is
                    handleLocation = (newY - minY) / (float) maxYDiff;
                }
            }
        });
    }

    /**
     * Gets the current value of the slider from 0 to 1
     *
     * @return The current value of the slider
     */
    public float getValue()
    {
        return this.handleLocation;
    }
}
