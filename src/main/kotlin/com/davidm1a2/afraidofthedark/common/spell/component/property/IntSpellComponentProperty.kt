package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.text.TranslationTextComponent

/**
 * Special spell component property that encapsulates integer parsing
 */
internal class IntSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Int) -> Unit,
    getter: (SpellComponentInstance<*>) -> Int,
    defaultValue: Int,
    private val minValue: Int?,
    private val maxValue: Int?
) : SpellComponentProperty<Int>(baseName, setter, getter, defaultValue) {
    override fun convertTo(newValue: String): Int {
        // Ensure the number is parsable
        val intValue = newValue.toIntOrNull()
            ?: throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.integer.format", newValue))

        // Ensure the int is valid
        if (minValue != null && intValue < minValue) {
            throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.integer.too_small", getName(), minValue))
        }
        if (maxValue != null && intValue > maxValue) {
            throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.integer.too_large", getName(), maxValue))
        }
        return intValue
    }
}