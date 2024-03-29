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
    override fun build(): SpellComponentProperty<Long> {
        return LongSpellComponentProperty(baseName!!, setter!!, getter!!, defaultValue!!, minValue, maxValue)
    }
}