package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.text.TranslationTextComponent

/**
 * Special spell component property that allows for an enumerated set of options
 *
 * @constructor just sets all the fields
 * @param baseName     The name of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value to use if the input is invalid
 */
internal class EnumSpellComponentProperty<T : Enum<T>>(
    baseName: String,
    setter: (SpellComponentInstance<*>, T) -> Unit,
    getter: (SpellComponentInstance<*>) -> T,
    private val defaultValue: T,
    val values: Array<T>
) : SpellComponentProperty(
    baseName,
    { instance, newValue ->
        val enumValue = values.find { it.name.equals(newValue, true) }
        if (enumValue != null) {
            setter(instance, enumValue)
        } else {
            setter(instance, defaultValue)
            throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.enum.format", newValue))
        }
    },
    {
        getter(it).toString()
    },
    {
        setter(it, defaultValue)
    }
)
