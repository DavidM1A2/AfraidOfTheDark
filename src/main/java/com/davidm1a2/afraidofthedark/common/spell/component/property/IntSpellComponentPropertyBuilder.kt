package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for int spell component property
 */
class IntSpellComponentPropertyBuilder : BoundedSpellComponentPropertyBuilder<Int, IntSpellComponentPropertyBuilder>()
{
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    fun build(): SpellComponentProperty
    {
        return IntSpellComponentProperty(name!!, description!!, setter!!, getter!!, defaultValue!!, minValue, maxValue)
    }
}