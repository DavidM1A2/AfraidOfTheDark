package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.text.TranslationTextComponent

/**
 * Special spell component property that encapsulates long parsing
 * @param baseName     The name of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value of the property
 * @param minValue     The minimum value of the property
 * @param maxValue     The maximum value of the property
 */
internal class LongSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Long) -> Unit,
    getter: (SpellComponentInstance<*>) -> Long,
    defaultValue: Long,
    minValue: Long?,
    maxValue: Long?
) : SpellComponentProperty(
    baseName,
    { instance, newValue ->
        // Ensure the number is parsable
        val longValue = newValue.toLongOrNull() ?: throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.long.format", newValue))

        // Ensure the long is valid
        if (minValue != null && longValue < minValue) {
            setter(instance, defaultValue)
            throw InvalidValueException(
                TranslationTextComponent(
                    "property_error.afraidofthedark.long.too_small",
                    TranslationTextComponent("$baseName.name"),
                    minValue
                )
            )
        }
        if (maxValue != null && longValue > maxValue) {
            setter(instance, defaultValue)
            throw InvalidValueException(
                TranslationTextComponent(
                    "property_error.afraidofthedark.long.too_large",
                    TranslationTextComponent("$baseName.name"),
                    maxValue
                )
            )
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