package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent

/**
 * Class containing information about a property of a spell component (power source, effect, or delivery method)
 * that is used by the UI to edit the value
 *
 * @constructor just sets all the fields
 * @property baseName The name of the property being edited
 * @property setter   The setter to be used when setting this property's value
 * @property getter   The getter to get this property's current value
 */
abstract class SpellComponentProperty<T>(
    private val baseName: String,
    private val setter: (SpellComponentInstance<*>, T) -> Unit,
    private val getter: (SpellComponentInstance<*>) -> T,
    private val defaultValue: T
) {
    internal abstract fun convertTo(newValue: String): T
    open fun convertFrom(value: T): String {
        return value.toString()
    }

    fun setValue(instance: SpellComponentInstance<*>, newValue: String) {
        try {
            setter(instance, convertTo(newValue))
        } catch (e: InvalidValueException) {
            setDefaultValue(instance)
            throw e
        }
    }

    fun setDefaultValue(instance: SpellComponentInstance<*>) {
        setter(instance, defaultValue)
    }

    fun getValue(instance: SpellComponentInstance<*>): String {
        val value = try {
            getter(instance)
        } catch (e: Exception) {
            // This block may happen if the NBT is tampered with
            defaultValue
        }
        return convertFrom(value)
    }

    fun getName(): Component {
        return TranslatableComponent("$baseName.name")
    }

    fun getDescription(): Component {
        return TranslatableComponent("$baseName.description")
    }
}
