package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.network.chat.TranslatableComponent

/**
 * Special spell component property that encapsulates long parsing
 */
internal class LongSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Long) -> Unit,
    getter: (SpellComponentInstance<*>) -> Long,
    defaultValue: Long,
    private val minValue: Long?,
    private val maxValue: Long?
) : SpellComponentProperty<Long>(baseName, setter, getter, defaultValue) {
    override fun convertTo(newValue: String): Long {
        // Ensure the number is parsable
        val longValue = newValue.toLongOrNull()
            ?: throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.long.format", newValue))

        // Ensure the long is valid
        if (minValue != null && longValue < minValue) {
            throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.long.too_small", getName(), minValue))
        }
        if (maxValue != null && longValue > maxValue) {
            throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.long.too_large", getName(), maxValue))
        }
        return longValue
    }
}