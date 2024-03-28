package com.davidm1a2.afraidofthedark.common.spell.component

import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty
import net.minecraft.resources.ResourceLocation

/**
 * Editable spell component piece
 */
abstract class SpellComponent<T : SpellComponent<T>>(
    id: ResourceLocation,
    icon: ResourceLocation,
    prerequisiteResearch: Research?
) : SpellComponentBase<T>(id, icon, prerequisiteResearch) {
    private val editableProperties: MutableList<SpellComponentProperty<*>> = mutableListOf()

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