package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Base builder for all spell component properties with min and max values
 *
 * @param <T> The type that this property is
 * @param <V> The builder type, used to
 * @property minValue The minimum value of this bounded value
 * @property maxValue The maximum value of this bounded value
 */
@Suppress("UNCHECKED_CAST")
abstract class BoundedSpellComponentPropertyBuilder<T, V : BoundedSpellComponentPropertyBuilder<T, V>> :
    SpellComponentPropertyBuilder<T, V>() {
    var minValue: T? = null
    var maxValue: T? = null

    /**
     * Sets the minimum value of the property
     *
     * @param minValue The minimum value of the property
     * @return The builder instance
     */
    fun withMinValue(minValue: T): V {
        this.minValue = minValue
        return this as V
    }

    /**
     * Sets the maximum value of the property
     *
     * @param maxValue The maximum value of the property
     * @return The builder instance
     */
    fun withMaxValue(maxValue: T): V {
        this.maxValue = maxValue
        return this as V
    }
}