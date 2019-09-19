package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;

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
     * Called when a mouse button event is fired
     *
     * @param event The event containing information about the mouse event
     */
    @Override
    public void fire(AOTDMouseEvent event)
    {
        ((IAOTDMouseListener) listener1).fire(event);
        ((IAOTDMouseListener) listener2).fire(event);
    }

    /**
     * Called when a mouse move event is fired
     *
     * @param event The event containing information about the mouse move event
     */
    @Override
    public void fire(AOTDMouseMoveEvent event)
    {
        ((IAOTDMouseMoveListener) listener1).fire(event);
        ((IAOTDMouseMoveListener) listener2).fire(event);
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
