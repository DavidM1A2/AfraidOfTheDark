package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for boolean spell component property
 */
class BooleanSpellComponentPropertyBuilder : SpellComponentPropertyBuilder<Boolean, BooleanSpellComponentPropertyBuilder>()
{
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    fun build(): SpellComponentProperty
    {
        return BooleanSpellComponentProperty(name!!, description!!, setter!!, getter!!, defaultValue!!)
    }
}