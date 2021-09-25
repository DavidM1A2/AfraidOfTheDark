package com.davidm1a2.afraidofthedark.common.spell.component

import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistryEntry

/**
 * Class representing a base for spell delivery methods, power sources, and effects to be registered
 * in the forge registry
 *
 * @constructor sets the entry id, icon, and factory
 * @param <T> The type that will be registered
 * @param <V> The type that this entry will create
 * @param id The ID of the entry to register
 * @property icon A resource location containing an image file with the icon to be used by the component
 */
abstract class SpellComponent<T : IForgeRegistryEntry<T>>(id: ResourceLocation, val icon: ResourceLocation) : ForgeRegistryEntry<T>() {
    private val editableProperties: MutableList<SpellComponentProperty<*>> = mutableListOf()

    init {
        registryName = id
    }

    abstract fun getUnlocalizedBaseName(): String

    fun getUnlocalizedName(): String {
        return "${getUnlocalizedBaseName()}.name"
    }

    fun getUnlocalizedPropertyBaseName(propertyName: String): String {
        return "${getUnlocalizedBaseName()}.$propertyName"
    }

    /**
     * Adds an editable property that this spell component has
     *
     * @param property A property that this component has that can be edited
     */
    fun addEditableProperty(property: SpellComponentProperty<*>) {
        editableProperties.add(property)
    }

    /**
     * @return An unmodifiable list of editable component properties
     */
    fun getEditableProperties(): List<SpellComponentProperty<*>> {
        return editableProperties
    }
}