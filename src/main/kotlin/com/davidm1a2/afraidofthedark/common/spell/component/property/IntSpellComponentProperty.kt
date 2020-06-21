package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance

/**
 * Special spell component property that encapsulates integer parsing
 *
 * @constructor just sets all the fields
 * @param name         The name of the property being edited
 * @param description  The description of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value of the property
 * @param minValue     The minimum value of the property
 * @param maxValue     The maximum value of the property
 */
internal class IntSpellComponentProperty(
    name: String,
    description: String,
    setter: (SpellComponentInstance<*>, Int) -> Unit,
    getter: (SpellComponentInstance<*>) -> Int,
    defaultValue: Int,
    minValue: Int?,
    maxValue: Int?
) : SpellComponentProperty(
    name,
    description,
    { instance, newValue ->
        // Ensure the number is parsable
        val intValue = newValue.toIntOrNull() ?: throw InvalidValueException("$newValue is not a valid integer!")

        // Ensure the int is valid
        if (minValue != null && intValue < minValue) {
            setter(instance, defaultValue)
            throw InvalidValueException("$name must be larger than or equal to $minValue")
        }
        if (maxValue != null && intValue > maxValue) {
            setter(instance, defaultValue)
            throw InvalidValueException("$name must be smaller than than or equal to $maxValue")
        }
        setter(instance, intValue)
    },
    {
        getter(it).toString()
    },
    {
        setter(it, defaultValue)
    }
)
