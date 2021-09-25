package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.text.TranslationTextComponent

/**
 * Special spell component property that encapsulates double parsing
 */
internal class DoubleSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Double) -> Unit,
    getter: (SpellComponentInstance<*>) -> Double,
    defaultValue: Double,
    private val minValue: Double?,
    private val maxValue: Double?
) : SpellComponentProperty<Double>(baseName, setter, getter, defaultValue) {
    override fun convertTo(newValue: String): Double {
        // Ensure the number is parsable
        val doubleValue = newValue.toDoubleOrNull()
            ?: throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.double.format", newValue))

        // Ensure the double is valid
        if (minValue != null && doubleValue < minValue) {
            throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.double.too_small", getName(), minValue))
        }
        if (maxValue != null && doubleValue > maxValue) {
            throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.double.too_large", getName(), maxValue))
        }
        return doubleValue
    }
}
