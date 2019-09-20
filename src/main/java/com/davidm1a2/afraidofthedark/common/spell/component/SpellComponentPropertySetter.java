package com.davidm1a2.afraidofthedark.common.spell.component;

/**
 * Utility interface containing a method with the logic to set a property in an NBT tag compound
 */
public interface SpellComponentPropertySetter
{
    /**
     * A setter function used to set the new value of the property, will return null if
     * the value was set successfully or a string containing error information if something
     * went wrong.
     *
     * @param newValue The new value of the property in string form to be parsed
     * @return An error message if something went wrong or null if the property was set successfully
     */
    String setProperty(String newValue);
}
