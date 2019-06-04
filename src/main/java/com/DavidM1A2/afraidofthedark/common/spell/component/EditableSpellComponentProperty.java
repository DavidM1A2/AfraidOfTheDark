package com.DavidM1A2.afraidofthedark.common.spell.component;

import java.util.function.Supplier;

/**
 * Class containing information about a property of a spell component (power source, effect, or delivery method)
 * that is used by the UI to edit the value
 */
public class EditableSpellComponentProperty
{
    // The user-friendly name of the property being edited
    private final String propertyName;
    // The description of the property being edited
    private final String propertyDescription;
    // The setter to be used when setting this property's value
    private final SpellComponentPropertySetter propertySetter;
    // The getter to get this property's current value
    private final Supplier<String> propertyGetter;

    /**
     * Constructor just sets all the fields
     *
     * @param propertyName        The name of the property being edited
     * @param propertyDescription The description of the property being edited
     * @param propertyGetter      The getter to get this property's current value
     * @param propertySetter      The setter to be used when setting this property's value
     */
    public EditableSpellComponentProperty(String propertyName, String propertyDescription, Supplier<String> propertyGetter, SpellComponentPropertySetter propertySetter)
    {
        this.propertyName = propertyName;
        this.propertyDescription = propertyDescription;
        this.propertyGetter = propertyGetter;
        this.propertySetter = propertySetter;
    }

    ///
    /// Getters for the fields
    ///

    public String getPropertyName()
    {
        return propertyName;
    }

    public String getPropertyDescription()
    {
        return propertyDescription;
    }

    public Supplier<String> getPropertyGetter()
    {
        return propertyGetter;
    }

    public SpellComponentPropertySetter getPropertySetter()
    {
        return propertySetter;
    }
}
