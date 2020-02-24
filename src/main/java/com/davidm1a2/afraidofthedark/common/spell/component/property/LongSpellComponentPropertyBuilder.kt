package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for long spell component property
 */
class LongSpellComponentPropertyBuilder :
    BoundedSpellComponentPropertyBuilder<Long, LongSpellComponentPropertyBuilder>() {
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    fun build(): SpellComponentProperty {
        return LongSpellComponentProperty(name!!, description!!, setter!!, getter!!, defaultValue!!, minValue, maxValue)
    }
}