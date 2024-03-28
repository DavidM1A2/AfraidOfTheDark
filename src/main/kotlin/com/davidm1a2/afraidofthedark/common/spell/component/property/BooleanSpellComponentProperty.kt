package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.network.chat.TranslatableComponent

/**
 * Special spell component property that encapsulates boolean parsing
 *
 * @constructor just sets all the fields
 * @param baseName         The name of the property being edited
 * @param getter       The getter to get this property's current value
 * @param setter       The setter to be used when setting this property's value
 * @param defaultValue The default value to use if the input is invalid
 */
internal class BooleanSpellComponentProperty(
    baseName: String,
    setter: (SpellComponentInstance<*>, Boolean) -> Unit,
    getter: (SpellComponentInstance<*>) -> Boolean,
    defaultValue: Boolean
) : SpellComponentProperty<Boolean>(baseName, setter, getter, defaultValue) {
    override fun convertTo(newValue: String): Boolean {
        // Ensure the boolean is valid
        return when {
            newValue.equals("false", true) -> false
            newValue.equals("true", true) -> true
            else -> throw InvalidValueException(TranslatableComponent("property_error.afraidofthedark.boolean.format", newValue))
        }
    }
}