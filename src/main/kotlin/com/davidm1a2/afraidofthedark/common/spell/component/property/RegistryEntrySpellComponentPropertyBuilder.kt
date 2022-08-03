package com.davidm1a2.afraidofthedark.common.spell.component.property

import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

class RegistryEntrySpellComponentPropertyBuilder<V : IForgeRegistryEntry<V>> :
    SpellComponentPropertyBuilder<V, RegistryEntrySpellComponentPropertyBuilder<V>>() {
    private var registry: IForgeRegistry<V>? = null

    fun withRegistry(registry: IForgeRegistry<V>): RegistryEntrySpellComponentPropertyBuilder<V> {
        this.registry = registry
        return this
    }

    override fun build(): SpellComponentProperty<V> {
        return RegistryEntrySpellComponentProperty(registry!!, baseName!!, setter!!, getter!!, defaultValue!!)
    }
}