package com.davidm1a2.afraidofthedark.common.spell.component.property;

/**
 * Base builder for all spell component properties with min and max values
 *
 * @param <T> The type that this property is
 * @param <V> The builder type, used to
 */
public abstract class BoundedSpellComponentPropertyBuilder<T, V extends BoundedSpellComponentPropertyBuilder<T, V>> extends SpellComponentPropertyBuilder<T, V>
{
    // The min and max of the value
    T minValue;
    T maxValue;

    /**
     * Sets the minimum value of the property
     *
     * @param minValue The minimum value of the property
     * @return The builder instance
     */
    @SuppressWarnings("unchecked")
    public V withMinValue(T minValue)
    {
        this.minValue = minValue;
        return (V) this;
    }

    /**
     * Sets the maximum value of the property
     *
     * @param maxValue The maximum value of the property
     * @return The builder instance
     */
    @SuppressWarnings("unchecked")
    public V withMaxValue(T maxValue)
    {
        this.maxValue = maxValue;
        return (V) this;
    }
}
