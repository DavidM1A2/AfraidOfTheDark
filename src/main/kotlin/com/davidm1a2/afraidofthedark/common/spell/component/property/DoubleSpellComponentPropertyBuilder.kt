package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for double spell component property
 */
class DoubleSpellComponentPropertyBuilder :
    BoundedSpellComponentPropertyBuilder<Double, DoubleSpellComponentPropertyBuilder>() {
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    fun build(): SpellComponentProperty {
        return DoubleSpellComponentProperty(
            name!!,
            description!!,
            setter!!,
            getter!!,
            defaultValue!!,
            minValue,
            maxValue
        )
    }
}