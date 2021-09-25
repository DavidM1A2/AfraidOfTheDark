package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.text.TranslationTextComponent

/**
 * Special spell component property that encapsulates integer parsing
 *
 * @constructor just sets all the fields
 * @param baseName     The name of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value of the property
 * @param minValue     The minimum value of the property
 * @param maxValue     The maximum value of the property
 */
internal class IntSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Int) -> Unit,
    getter: (SpellComponentInstance<*>) -> Int,
    defaultValue: Int,
    minValue: Int?,
    maxValue: Int?
) : SpellComponentProperty(
    baseName,
    { instance, newValue ->
        // Ensure the number is parsable
        val intValue =
            newValue.toIntOrNull() ?: throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.integer.format", newValue))

        // Ensure the int is valid
        if (minValue != null && intValue < minValue) {
            setter(instance, defaultValue)
            throw InvalidValueException(
                TranslationTextComponent(
                    "property_error.afraidofthedark.integer.too_small",
                    TranslationTextComponent("$baseName.name"),
                    minValue
                )
            )
        }
        if (maxValue != null && intValue > maxValue) {
            setter(instance, defaultValue)
            throw InvalidValueException(
                TranslationTextComponent(
                    "property_error.afraidofthedark.integer.too_large",
                    TranslationTextComponent("$baseName.name"),
                    maxValue
                )
            )
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
