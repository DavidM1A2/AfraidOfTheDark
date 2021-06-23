package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for boolean spell component property
 */
class EnumSpellComponentPropertyBuilder :
    SpellComponentPropertyBuilder<Int, EnumSpellComponentPropertyBuilder>() {
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    fun build(values: List<String>): SpellComponentProperty {
        return EnumSpellComponentProperty(name!!, description!!, setter!!, getter!!, defaultValue!!, values)
    }
}