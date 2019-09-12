package com.DavidM1A2.afraidofthedark.common.spell.component.property;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Class containing information about a property of a spell component (power source, effect, or delivery method)
 * that is used by the UI to edit the value
 */
public class SpellComponentProperty
{
    // The user-friendly name of the property being edited
    private final String name;
    // The description of the property being edited
    private final String description;
    // The setter to be used when setting this property's value
    private final Consumer<String> setter;
    // The getter to get this property's current value
    private final Supplier<String> getter;

    /**
     * Constructor just sets all the fields
     *
     * @param name        The name of the property being edited
     * @param description The description of the property being edited
     * @param setter      The setter to be used when setting this property's value
     * @param getter      The getter to get this property's current value
     */
    public SpellComponentProperty(String name, String description, Consumer<String> setter, Supplier<String> getter)
    {
        this.name = name;
        this.description = description;
        this.setter = setter;
        this.getter = getter;
    }

    ///
    /// Getters for the fields
    ///

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public Consumer<String> getSetter()
    {
        return setter;
    }

    public Supplier<String> getGetter()
    {
        return getter;
    }
}
