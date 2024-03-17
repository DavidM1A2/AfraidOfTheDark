package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.gui.FontCache.fontMap
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.FontLoader
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import net.minecraft.resources.ResourceLocation

/**
 * A singleton class containing temporary client data to store
 *
 * @property fontMap A mapping of font size -> font object used to render text
 */
object FontCache {
    private val fontMap = mutableMapOf<Float, TrueTypeFont>()

    /**
     * Getter for mod font based on a font size. If the object is not yet cached, create it
     *
     * @param fontSize The size of the font to get
     * @return The font object to get
     */
    fun getOrCreate(fontSize: Float): TrueTypeFont {
        // If the font map does not contain the size, create that font size and store it
        return fontMap.computeIfAbsent(fontSize) {
            FontLoader.createFont(ResourceLocation("afraidofthedark:fonts/caveat_bush.ttf"), it)
        }
    }
}