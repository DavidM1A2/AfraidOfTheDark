package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.text.TranslationTextComponent

/**
 * Special spell component property that encapsulates double parsing
 */
internal class DoubleSpellComponentProperty
/**
 * Constructor just sets all the fields
 *
 * @param baseName     The name of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value of the property
 * @param minValue     The minimum value of the property
 * @param maxValue     The maximum value of the property
 */(
    baseName: String,
    setter: (SpellComponentInstance<*>, Double) -> Unit,
    getter: (SpellComponentInstance<*>) -> Double,
    defaultValue: Double,
    minValue: Double?,
    maxValue: Double?
) : SpellComponentProperty(
    baseName,
    { instance, newValue ->
        // Ensure the number is parsable
        val doubleValue =
            newValue.toDoubleOrNull() ?: throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.double.format", newValue))

        // Ensure the double is valid
        if (minValue != null && doubleValue < minValue) {
            setter(instance, defaultValue)
            throw InvalidValueException(
                TranslationTextComponent(
                    "property_error.afraidofthedark.double.too_small",
                    TranslationTextComponent("$baseName.name"),
                    minValue
                )
            )
        }
        if (maxValue != null && doubleValue > maxValue) {
            setter(instance, defaultValue)
            throw InvalidValueException(
                TranslationTextComponent(
                    "property_error.afraidofthedark.double.too_large",
                    TranslationTextComponent("$baseName.name"),
                    maxValue
                )
            )
        }
        setter(instance, doubleValue)
    },
    {
        getter(it).toString()
    },
    {
        setter(it, defaultValue)
    }
)