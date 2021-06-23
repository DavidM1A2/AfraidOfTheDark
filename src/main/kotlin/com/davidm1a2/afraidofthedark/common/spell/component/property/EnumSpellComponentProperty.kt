package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance

/**
 * Special spell component property that allows for an enumerated set of options
 *
 * @constructor just sets all the fields
 * @param name         The name of the property being edited
 * @param description  The description of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value to use if the input is invalid
 */
internal class EnumSpellComponentProperty(
    name: String,
    description: String,
    setter: (SpellComponentInstance<*>, Int) -> Unit,
    getter: (SpellComponentInstance<*>) -> Int,
    val defaultValue: Int,
    val values: List<String>
) : SpellComponentProperty(
    name,
    description,
    { instance, newValue ->
        // Ensure the boolean is valid
        when {
            values.contains(newValue) -> {
                setter(instance, values.indexOf(newValue))
            }
            else -> {
                if (defaultValue in values.indices) {
                    setter(instance, defaultValue)
                } else {
                    setter(instance, -1)
                }
                throw InvalidValueException("$newValue must in the list of options!")
            }
        }
    },
    {
        getter(it).toString()
    },
    {
        setter(it, defaultValue)
    }
)
