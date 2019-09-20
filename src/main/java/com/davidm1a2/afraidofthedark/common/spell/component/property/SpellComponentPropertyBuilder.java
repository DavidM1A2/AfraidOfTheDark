package com.davidm1a2.afraidofthedark.common.spell.component.property;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base builder for all spell component property builders
 *
 * @param <T> The type that this property is
 * @param <V> The builder type, used to
 */
public abstract class SpellComponentPropertyBuilder<T, V extends SpellComponentPropertyBuilder<T, V>>
{
    // The name of the property
    String name;
    // The description of the property
    String description;
    // The setter for this property
    Consumer<T> setter;
    // The getter for this property
    Supplier<T> getter;
    // The default value of this property
    T defaultValue;

    /**
     * Sets the name of the property
     *
     * @param name The name of the property
     * @return The builder instance
     */
    @SuppressWarnings("unchecked")
    public V withName(String name)
    {
        this.name = name;
        return (V) this;
    }

    /**
     * Sets the description of the property
     *
     * @param description The description of the property
     * @return The builder instance
     */
    @SuppressWarnings("unchecked")
    public V withDescription(String description)
    {
        this.description = description;
        return (V) this;
    }

    /**
     * Sets the setter of the property
     *
     * @param setter The setter of the property
     * @return The builder instance
     */
    @SuppressWarnings("unchecked")
    public V withSetter(Consumer<T> setter)
    {
        this.setter = setter;
        return (V) this;
    }

    /**
     * Sets the getter of the property
     *
     * @param getter The getter of the property
     * @return The builder instance
     */
    @SuppressWarnings("unchecked")
    public V withGetter(Supplier<T> getter)
    {
        this.getter = getter;
        return (V) this;
    }

    /**
     * Sets the default value of the property
     *
     * @param defaultValue The default value of the property
     * @return The builder instance
     */
    @SuppressWarnings("unchecked")
    public V withDefaultValue(T defaultValue)
    {
        this.defaultValue = defaultValue;
        return (V) this;
    }
}
