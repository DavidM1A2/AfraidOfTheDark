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
    override fun build(): SpellComponentProperty<Double> {
        return DoubleSpellComponentProperty(
            baseName!!,
            setter!!,
            getter!!,
            defaultValue!!,
            minValue,
            maxValue
        )
    }
}