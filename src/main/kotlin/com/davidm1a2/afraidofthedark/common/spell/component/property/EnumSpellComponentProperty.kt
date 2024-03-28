package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.network.chat.TranslatableComponent

/**
 * Special spell component property that allows for an enumerated set of options
 */
internal class EnumSpellComponentProperty<T : Enum<T>>(
    baseName: String,
    setter: (SpellComponentInstance<*>, T) -> Unit,
    getter: (SpellComponentInstance<*>) -> T,
    defaultValue: T,
    val values: Array<T>
) : SpellComponentProperty<T>(baseName, setter, getter, defaultValue) {
    override fun convertTo(newValue: String): T {
        val enumValue = values.find { it.name.equals(newValue, true) }
        if (enumValue == null) {
            throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.enum.format", newValue))
        } else {
            return enumValue
        }
    }
}
