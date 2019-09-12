package com.DavidM1A2.afraidofthedark.common.spell.component;

/**
 * An exception that gets thrown when an editable spell component property gets an invalid value
 */
public class InvalidValueException extends RuntimeException
{
    /**
     * Constructor that takes in an error message to be displayed to the user
     *
     * @param message the detail message
     */
    public InvalidValueException(String message)
    {
        super(message);
    }
}
