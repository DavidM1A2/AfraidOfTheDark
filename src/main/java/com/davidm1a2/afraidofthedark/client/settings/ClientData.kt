package com.davidm1a2.afraidofthedark.client.settings

import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.FontLoader.createFont
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.util.ResourceLocation

/**
 * A singleton class containing temporary client data to store
 */
object ClientData
{
    // A mapping of font size -> font object used to render text
    private val fontMap: MutableMap<Float, TrueTypeFont> = mutableMapOf()

    // A field that will keep track of which research is currently selected
    var lastSelectedResearch: Research? = null

    // A field that will keep track of which spell is currently selected
    var lastSelectedSpell: Spell? = null

    /**
     * Getter for TargaMSHand font based on a font size. If the object is not yet cached, create it
     *
     * @param fontSize The size of the font to get
     * @return The font object to get
     */
    fun getTargaMSHandFontSized(fontSize: Float): TrueTypeFont
    {
        // If the font map does not contain the size, create that font size and store it
        if (!fontMap.containsKey(fontSize))
        {
            // Put the font size with the newly loaded font
            val font = createFont(ResourceLocation("afraidofthedark:fonts/targa_ms_hand.ttf"), fontSize, true)
            fontMap[fontSize] = font
        }
        // Get the font from the map
        return fontMap[fontSize]!!
    }
}