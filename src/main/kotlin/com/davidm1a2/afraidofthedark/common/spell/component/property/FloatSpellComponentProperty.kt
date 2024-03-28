package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.network.chat.TranslatableComponent

/**
 * Special spell component property that encapsulates float parsing
 */
internal class FloatSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Float) -> Unit,
    getter: (SpellComponentInstance<*>) -> Float,
    defaultValue: Float,
    private val minValue: Float?,
    private val maxValue: Float?
) : SpellComponentProperty<Float>(baseName, setter, getter, defaultValue) {
    override fun convertTo(newValue: String): Float {
        // Ensure the number is parsable
        val floatValue = newValue.toFloatOrNull()
            ?: throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.float.format", newValue))

        // Ensure the float is valid
        if (minValue != null && floatValue < minValue) {
            throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.float.too_small", getName(), minValue))
        }
        if (maxValue != null && floatValue > maxValue) {
            throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.float.too_large", getName(), maxValue))
        }
        return floatValue
    }
}