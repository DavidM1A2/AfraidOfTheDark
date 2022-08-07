package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

internal class RegistryEntrySpellComponentProperty<T : IForgeRegistryEntry<T>>(
    private val registry: IForgeRegistry<T>,
    private val filter: ((T) -> Boolean)?,
    baseName: String,
    setter: (SpellComponentInstance<*>, T) -> Unit,
    getter: (SpellComponentInstance<*>) -> T,
    defaultValue: T
) : SpellComponentProperty<T>(baseName, setter, getter, defaultValue) {
    override fun convertFrom(value: T): String {
        return value.registryName!!.toString()
    }

    override fun convertTo(newValue: String): T {
        val key = ResourceLocation.tryParse(newValue) ?: throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.registry_entry.invalid_resource_location", newValue))

        val value = registry.getValue(key) ?: throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.registry_entry.missing_sound", newValue))

        return if (filter == null || filter.invoke(value)) {
            value
        } else {
            throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.registry_entry.disallowed_entry", newValue))
        }
    }
}