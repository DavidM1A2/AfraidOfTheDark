package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for int spell component property
 */
class IntSpellComponentPropertyBuilder : BoundedSpellComponentPropertyBuilder<Int, IntSpellComponentPropertyBuilder>() {
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    override fun build(): SpellComponentProperty<Int> {
        return IntSpellComponentProperty(baseName!!, setter!!, getter!!, defaultValue!!, minValue, maxValue)
    }
}