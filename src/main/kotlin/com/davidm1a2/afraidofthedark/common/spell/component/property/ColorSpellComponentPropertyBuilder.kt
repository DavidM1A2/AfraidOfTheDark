package com.davidm1a2.afraidofthedark.common.spell.component.property

import java.awt.Color

/**
 * Builder for color spell component property
 */
class ColorSpellComponentPropertyBuilder :
    SpellComponentPropertyBuilder<Color, ColorSpellComponentPropertyBuilder>() {
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    fun build(): SpellComponentProperty<Color> {
        return ColorSpellComponentProperty(baseName!!, setter!!, getter!!, defaultValue!!)
    }
}