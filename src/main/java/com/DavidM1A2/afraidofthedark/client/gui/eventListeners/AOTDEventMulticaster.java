package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;

/**
 * Class used to add multiple event listeners to a given control. This design pattern is copied from Java's Swing EventMulticaster
 */
public class AOTDEventMulticaster implements IAOTDKeyListener, IAOTDMouseListener, IAOTDMouseMoveListener
{
    // The first event listener to call
    private final IAOTDEventListener listener1;
    // The second event listener to call
    private final IAOTDEventListener listener2;

    /**
     * Constructor sets up the first and second listener references
     *
     * @param listener1 The first listener that this multicaster represents
     * @param listener2 The second listener that this multicaster represents
     */
    private AOTDEventMulticaster(IAOTDEventListener listener1, IAOTDEventListener listener2)
    {
        this.listener1 = listener1;
        this.listener2 = listener2;
    }

    /**
     * Static function to create a new event listener that is made up of two other event listeners. When the event is called both listeners are fired
     *
     * @param first  The first key listener to fire
     * @param second The second key listener to fire
     * @return The key listener that will fire both listeners when called
     */
    public static IAOTDKeyListener combineKeyListeners(IAOTDKeyListener first, IAOTDKeyListener second)
    {
        return (IAOTDKeyListener) combine(first, second);
    }

    /**
     * Static function to create a new event listener that is made up of two other event listeners. When the event is called both listeners are fired
     *
     * @param first  The first mouse listener to fire
     * @param second The second mouse listener to fire
     * @return The mouse listener that will fire both listeners when called
     */
    public static IAOTDMouseListener combineMouseListeners(IAOTDMouseListener first, IAOTDMouseListener second)
    {
        return (IAOTDMouseListener) combine(first, second);
    }

    /**
     * Static function to create a new event listener that is made up of two other event listeners. When the event is called both listeners are fired
     *
     * @param first  The first mouse move listener to fire
     * @param second The second mouse move listener to fire
     * @return The mouse listener that will fire both listeners when called
     */
    public static IAOTDMouseMoveListener combineMouseMoveListeners(IAOTDMouseMoveListener first, IAOTDMouseMoveListener second)
    {
        return (IAOTDMouseMoveListener) combine(first, second);
    }

    /**
     * Static function to create a new event listener that is made up of two other event listeners. When the event is called both listeners are fired
     *
     * @param first  The first listener to fire
     * @param second The second listener to fire
     * @return The listener that will fire both listeners when called
     */
    private static IAOTDEventListener combine(IAOTDEventListener first, IAOTDEventListener second)
    {
        // If either arguments are null just return the non-null argument
        if (first == null)
        {
            return second;
        }
        if (second == null)
        {
            return first;
        }
        // Return a multicaster with both arguments
        return new AOTDEventMulticaster(first, second);
    }

    /**
     * Called when a mouse is dragged, fires both listeners
     *
     * @param event The event containing information about the mouse drag
     */
    @Override
    public void mouseDragged(AOTDMouseEvent event)
    {
        ((IAOTDMouseMoveListener) listener1).mouseDragged(event);
        ((IAOTDMouseMoveListener) listener2).mouseDragged(event);
    }

    /**
     * Called when a mouse is moved, fires both listeners
     *
     * @param event The event containing information about the mouse move
     */
    @Override
    public void mouseMoved(AOTDMouseEvent event)
    {
        ((IAOTDMouseMoveListener) listener1).mouseMoved(event);
        ((IAOTDMouseMoveListener) listener2).mouseMoved(event);
    }

    /**
     * Called when a mouse button is pressed and released, fires both listeners
     *
     * @param event The event containing information about the mouse click
     */
    @Override
    public void mouseClicked(AOTDMouseEvent event)
    {
        ((IAOTDMouseListener) listener1).mouseClicked(event);
        ((IAOTDMouseListener) listener2).mouseClicked(event);
    }

    /**
     * Called when a mouse button is pressed, fires both listeners
     *
     * @param event The event containing information about the mouse press
     */
    @Override
    public void mousePressed(AOTDMouseEvent event)
    {
        ((IAOTDMouseListener) listener1).mousePressed(event);
        ((IAOTDMouseListener) listener2).mousePressed(event);
    }

    /**
     * Called when a mouse button is released, fires both listeners
     *
     * @param event The event containing information about the mouse release
     */
    @Override
    public void mouseReleased(AOTDMouseEvent event)
    {
        ((IAOTDMouseListener) listener1).mouseReleased(event);
        ((IAOTDMouseListener) listener2).mouseReleased(event);
    }

    /**
     * Called when the mouse is moved into the bounding box of a control, fires both listeners
     *
     * @param event The event containing information about the mouse entering event
     */
    @Override
    public void mouseEntered(AOTDMouseEvent event)
    {
        ((IAOTDMouseListener) listener1).mouseEntered(event);
        ((IAOTDMouseListener) listener2).mouseEntered(event);
    }

    /**
     * Called when the mouse is moved out of the bounding box of a control, fires both listeners
     *
     * @param event The event containing information about the mouse exiting event
     */
    @Override
    public void mouseExited(AOTDMouseEvent event)
    {
        ((IAOTDMouseListener) listener1).mouseExited(event);
        ((IAOTDMouseListener) listener2).mouseExited(event);
    }

    /**
     * Called when a key is typed (pressed and released), fires both listeners
     *
     * @param event The event containing information about the key press
     */
    @Override
    public void keyTyped(AOTDKeyEvent event)
    {
        ((IAOTDKeyListener) listener1).keyTyped(event);
        ((IAOTDKeyListener) listener2).keyTyped(event);
    }

    /**
     * Called when a key is pressed down, fires both listeners
     *
     * @param event The event containing information about the key press
     */
    @Override
    public void keyPressed(AOTDKeyEvent event)
    {
        ((IAOTDKeyListener) listener1).keyPressed(event);
        ((IAOTDKeyListener) listener2).keyPressed(event);
    }

    /**
     * Called when a key is released, fires both listeners
     *
     * @param event The event containing information about the key release
     */
    @Override
    public void keyReleased(AOTDKeyEvent event)
    {
        ((IAOTDKeyListener) listener1).keyReleased(event);
        ((IAOTDKeyListener) listener2).keyReleased(event);
    }
}
