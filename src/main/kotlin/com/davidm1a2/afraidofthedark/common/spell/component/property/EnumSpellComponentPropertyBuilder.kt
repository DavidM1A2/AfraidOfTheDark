package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for boolean spell component property
 */
class EnumSpellComponentPropertyBuilder<T : Enum<T>>(private val values: Array<T>) :
    SpellComponentPropertyBuilder<T, EnumSpellComponentPropertyBuilder<T>>() {
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    fun build(): SpellComponentProperty {
        return EnumSpellComponentProperty(name!!, description!!, setter!!, getter!!, defaultValue!!, values)
    }
}