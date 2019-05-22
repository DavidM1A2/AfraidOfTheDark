package com.DavidM1A2.afraidofthedark.common.spell.component;

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

    /**
     * Constructor just sets all the fields
     *
     * @param propertyName        The name of the property being edited
     * @param propertyDescription The description of the property being edited
     * @param propertySetter      The setter to be used when setting this property's value
     */
    public EditableSpellComponentProperty(String propertyName, String propertyDescription, SpellComponentPropertySetter propertySetter)
    {
        this.propertyName = propertyName;
        this.propertyDescription = propertyDescription;
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

    public SpellComponentPropertySetter getPropertySetter()
    {
        return propertySetter;
    }
}
