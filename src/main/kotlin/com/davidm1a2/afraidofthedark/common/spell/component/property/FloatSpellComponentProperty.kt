package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.text.TranslationTextComponent

/**
 * Special spell component property that encapsulates float parsing
 *
 * @constructor just sets all the fields
 * @param baseName     The name of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value of the property
 * @param minValue     The minimum value of the property
 * @param maxValue     The maximum value of the property
 */
internal class FloatSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Float) -> Unit,
    getter: (SpellComponentInstance<*>) -> Float,
    defaultValue: Float,
    minValue: Float?,
    maxValue: Float?
) : SpellComponentProperty(
    baseName,
    { instance, newValue ->
        // Ensure the number is parsable
        val floatValue =
            newValue.toFloatOrNull() ?: throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.float.format", newValue))

        // Ensure the float is valid
        if (minValue != null && floatValue < minValue) {
            setter(instance, defaultValue)
            throw InvalidValueException(
                TranslationTextComponent(
                    "property_error.afraidofthedark.float.too_small",
                    TranslationTextComponent("$baseName.name"),
                    minValue
                )
            )
        }
        if (maxValue != null && floatValue > maxValue) {
            setter(instance, defaultValue)
            throw InvalidValueException(
                TranslationTextComponent(
                    "property_error.afraidofthedark.float.too_large", TranslationTextComponent("$baseName.name"),

                    maxValue
                )
            )
        }
        setter(instance, floatValue)
    },
    {
        getter(it).toString()
    },
    {
        setter(it, defaultValue)
    }
)
