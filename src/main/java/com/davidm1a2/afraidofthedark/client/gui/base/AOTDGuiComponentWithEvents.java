package com.davidm1a2.afraidofthedark.client.gui.base;

import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseScrollEvent;
import org.lwjgl.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A base class for any GUI components that can listen for action events like mouse clicks
 */
public abstract class AOTDGuiComponentWithEvents extends AOTDGuiComponent
{
    // The mouse listener of this component
    private List<Consumer<AOTDMouseEvent>> mouseListeners;
    // The mouse move listener of this component
    private List<Consumer<AOTDMouseMoveEvent>> mouseMoveListeners;
    // The mouse scroll listener of this component
    private List<Consumer<AOTDMouseScrollEvent>> mouseScrollListeners;
    // The key listener of this component
    private List<Consumer<AOTDKeyEvent>> keyListeners;

    /**
     * Constructor initializes the bounding box
     *
     * @param x      The X location of the top left corner
     * @param y      The Y location of the top left corner
     * @param width  The width of the component
     * @param height The height of the component
     */
    public AOTDGuiComponentWithEvents(Integer x, Integer y, Integer width, Integer height)
    {
        super(x, y, width, height);
        // Add a mouse move listener to this control that allows us to fire off events whenever conditions are met
        this.addMouseMoveListener(event ->
        {
            // If we move the mouse also fire other related events
            if (event.getEventType() == AOTDMouseMoveEvent.EventType.Move)
            {
                // Grab the source of the event
                AOTDGuiComponentWithEvents component = event.getSource();
                // Store the flag telling us if the component was hovered
                boolean wasHovered = component.isHovered();
                // Set the hovered flag based on if we intersect the component
                component.setHovered(component.intersects(new Point(event.getMouseX(), event.getMouseY())));
                // Fire mouse enter/exit events if our mouse entered or exited the control
                if (component.isHovered() && !wasHovered)
                {
                    component.processMouseMoveInput(new AOTDMouseMoveEvent(component, event.getMouseX(), event.getMouseY(), AOTDMouseMoveEvent.EventType.Enter));
                }
                if (!component.isHovered() && wasHovered)
                {
                    component.processMouseMoveInput(new AOTDMouseMoveEvent(component, event.getMouseX(), event.getMouseY(), AOTDMouseMoveEvent.EventType.Exit));
                }
            }
        });
    }

    /**
     * Called to process a mouse input event
     *
     * @param event The event to process
     */
    public void processMouseInput(AOTDMouseEvent event)
    {
        // If the event is consumed, don't do anything
        if (event.isConsumed())
        {
            return;
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        // If we have a mouse listener, process a mouse event
        if (mouseListeners != null)
        {
            // Get the event type and process accordingly
            mouseListeners.forEach(listener -> listener.accept(event));
        }
    }

    /**
     * Called to process a mouse move input event
     *
     * @param event The event to process
     */
    public void processMouseMoveInput(AOTDMouseMoveEvent event)
    {
        // If the event is consumed, don't do anything
        if (event.isConsumed())
        {
            return;
        }

        // If the event is an enter or exit event, consume the event. This is because an enter and exit event can only happen to a single control at a time
        if (event.getEventType() == AOTDMouseMoveEvent.EventType.Enter || event.getEventType() == AOTDMouseMoveEvent.EventType.Exit)
        {
            event.consume();
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        // If we have a mouse move listener, and the event is a move or drag, fire off our mouse move listener
        if (mouseMoveListeners != null)
        {
            mouseMoveListeners.forEach(listener -> listener.accept(event));
        }
    }

    /**
     * Called to process a mouse scroll input event
     *
     * @param event The event to process
     */
    public void processMouseScrollInput(AOTDMouseScrollEvent event)
    {
        // If the event is consumed, don't do anything
        if (event.isConsumed())
        {
            return;
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        // If we have a mouse scroll listener fire off our mouse scroll listener
        if (mouseScrollListeners != null)
        {
            mouseScrollListeners.forEach(listener -> listener.accept(event));
        }
    }

    /**
     * Called to process a key input event
     *
     * @param event The event to process
     */
    public void processKeyInput(AOTDKeyEvent event)
    {
        // If the event is consumed, don't do anything
        if (event.isConsumed())
        {
            return;
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        // If we have a key listener, process a key event
        if (keyListeners != null)
        {
            keyListeners.forEach(listener -> listener.accept(event));
        }
    }

    /**
     * Adds a mouse listener to this control to be fired whenever a mouse event is raised
     *
     * @param mouseListener The mouse listener to add
     */
    public void addMouseListener(Consumer<AOTDMouseEvent> mouseListener)
    {
        if (mouseListener == null)
        {
            return;
        }
        if (this.mouseListeners == null)
        {
            this.mouseListeners = new ArrayList<>(1);
        }
        // Combine the current mouse listener with the new one to make the next mouse listener
        this.mouseListeners.add(mouseListener);
    }

    /**
     * Adds a mouse move listener to this control to be fired whenever a mouse move event is raised
     *
     * @param mouseMoveListener The mouse move listener to add
     */
    public void addMouseMoveListener(Consumer<AOTDMouseMoveEvent> mouseMoveListener)
    {
        if (mouseMoveListener == null)
        {
            return;
        }
        if (this.mouseMoveListeners == null)
        {
            this.mouseMoveListeners = new ArrayList<>(1);
        }
        // Combine the current mouse move listener with the new one to make the next mouse move listener
        this.mouseMoveListeners.add(mouseMoveListener);
    }

    /**
     * Adds a mouse scroll listener to this control to be fired whenever a mouse scroll event is raised
     *
     * @param mouseScrollListener The mouse scroll listener to add
     */
    public void addMouseScrollListener(Consumer<AOTDMouseScrollEvent> mouseScrollListener)
    {
        if (mouseScrollListener == null)
        {
            return;
        }
        if (this.mouseScrollListeners == null)
        {
            this.mouseScrollListeners = new ArrayList<>(1);
        }
        // Combine the current mouse scroll listener with the new one to make the next mouse scroll listener
        this.mouseScrollListeners.add(mouseScrollListener);
    }

    /**
     * Adds a key listener to this control to be fired whenever a key event is raised
     *
     * @param keyListener The key listener to add
     */
    public void addKeyListener(Consumer<AOTDKeyEvent> keyListener)
    {
        if (keyListener == null)
        {
            return;
        }
        if (this.keyListeners == null)
        {
            this.keyListeners = new ArrayList<>(1);
        }
        // Combine the current key listener with the new one to make the next key listener
        this.keyListeners.add(keyListener);
    }
}
