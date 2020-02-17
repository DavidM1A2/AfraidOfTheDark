package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance

/**
 * Special spell component property that encapsulates long parsing
 * @param name         The name of the property being edited
 * @param description  The description of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value of the property
 * @param minValue     The minimum value of the property
 * @param maxValue     The maximum value of the property
 */
internal class LongSpellComponentProperty(
        name: String,
        description: String,
        setter: (SpellComponentInstance<*>, Long) -> Unit,
        getter: (SpellComponentInstance<*>) -> Long,
        defaultValue: Long,
        minValue: Long?,
        maxValue: Long?
) : SpellComponentProperty(
        name,
        description,
        { instance, newValue ->
            // Ensure the number is parsable
            val longValue = newValue.toLongOrNull() ?: throw InvalidValueException("$newValue is not a valid integer!")

            // Ensure the long is valid
            if (minValue != null && longValue < minValue)
            {
                setter(instance, defaultValue)
                throw InvalidValueException("$name must be larger than or equal to $minValue");
            }
            if (maxValue != null && longValue > maxValue)
            {
                setter(instance, defaultValue)
                throw InvalidValueException("$name must be smaller than than or equal to $maxValue");
            }
            setter(instance, longValue)
        },
        {
            getter(it).toString()
        },
        {
            setter(it, defaultValue)
        }
)