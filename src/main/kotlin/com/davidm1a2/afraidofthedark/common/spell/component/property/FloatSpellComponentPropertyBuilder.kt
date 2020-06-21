package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for float spell component property
 */
class FloatSpellComponentPropertyBuilder :
    BoundedSpellComponentPropertyBuilder<Float, FloatSpellComponentPropertyBuilder>() {
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    fun build(): SpellComponentProperty {
        return FloatSpellComponentProperty(
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