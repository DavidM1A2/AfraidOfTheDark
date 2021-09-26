package com.davidm1a2.afraidofthedark.client.settings

import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.FontLoader
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.settings.ClientData.fontMap
import net.minecraft.util.ResourceLocation

/**
 * A singleton class containing temporary client data to store
 *
 * @property fontMap A mapping of font size -> font object used to render text
 */
object ClientData {
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
            FontLoader.createFont(ResourceLocation("afraidofthedark:fonts/targa_ms_hand.ttf"), it)
        }
    }
}
