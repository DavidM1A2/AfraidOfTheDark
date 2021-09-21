package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance

/**
 * Base builder for all spell component property builders
 *
 * @param <T> The type that this property is
 * @param <V> The builder type, used to
 * @property baseName The name of the property
 * @property setter The setter for this property
 * @property setter The getter for this property
 * @property defaultValue The default value of this property
 */
@Suppress("UNCHECKED_CAST")
abstract class SpellComponentPropertyBuilder<T, V : SpellComponentPropertyBuilder<T, V>> {
    var baseName: String? = null
    var setter: ((SpellComponentInstance<*>, T) -> Unit)? = null
    var getter: ((SpellComponentInstance<*>) -> T)? = null
    var defaultValue: T? = null

    /**
     * Sets the name of the property
     *
     * @param baseName The name of the property
     * @return The builder instance
     */
    fun withBaseName(baseName: String): V {
        this.baseName = baseName
        return this as V
    }

    /**
     * Sets the setter of the property
     *
     * @param setter The setter of the property
     * @return The builder instance
     */
    fun withSetter(setter: ((SpellComponentInstance<*>, T) -> Unit)): V {
        this.setter = setter
        return this as V
    }

    /**
     * Sets the getter of the property
     *
     * @param getter The getter of the property
     * @return The builder instance
     */
    fun withGetter(getter: ((SpellComponentInstance<*>) -> T)): V {
        this.getter = getter
        return this as V
    }

    /**
     * Sets the default value of the property
     *
     * @param defaultValue The default value of the property
     * @return The builder instance
     */
    fun withDefaultValue(defaultValue: T): V {
        this.defaultValue = defaultValue
        return this as V
    }
}