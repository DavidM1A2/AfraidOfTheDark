package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.text.TranslatableComponent
import java.awt.Color

/**
 * Special spell component property that encapsulates color parsing
 */
internal class ColorSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Color) -> Unit,
    getter: (SpellComponentInstance<*>) -> Color,
    defaultValue: Color,
) : SpellComponentProperty<Color>(baseName, setter, getter, defaultValue) {
    override fun convertTo(newValue: String): Color {
        val rgbStrings = newValue.split(Regex("\\s+"))
        if (rgbStrings.size != 3) {
            throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.color.format"))
        }
        rgbStrings.forEach {
            it.toIntOrNull()?.let { rgb ->
                if (rgb < 0 || rgb > 255) {
                    throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.color.value_range"))
                }
            } ?: throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.color.value_type"))
        }
        return Color(rgbStrings[0].toInt(), rgbStrings[1].toInt(), rgbStrings[2].toInt())
    }

    override fun convertFrom(value: Color): String {
        return "${value.red} ${value.green} ${value.blue}"
    }
}