package com.davidm1a2.afraidofthedark.client.settings

import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.FontLoader
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.settings.ClientData.fontMap
import com.davidm1a2.afraidofthedark.client.settings.ClientData.lastSelectedResearch
import com.davidm1a2.afraidofthedark.client.settings.ClientData.lastSelectedSpell
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.util.ResourceLocation

/**
 * A singleton class containing temporary client data to store
 *
 * @property fontMap A mapping of font size -> font object used to render text
 * @property lastSelectedResearch A field that will keep track of which research is currently selected
 * @property lastSelectedSpell A field that will keep track of which spell is currently selected
 */
object ClientData
{
    private val fontMap = mutableMapOf<Float, TrueTypeFont>()
    var lastSelectedResearch: Research? = null
    var lastSelectedSpell: Spell? = null

    /**
     * Getter for mod font based on a font size. If the object is not yet cached, create it
     *
     * @param fontSize The size of the font to get
     * @return The font object to get
     */
    fun getOrCreate(fontSize: Float): TrueTypeFont
    {
        // If the font map does not contain the size, create that font size and store it
        if (!fontMap.containsKey(fontSize))
        {
            // Put the font size with the newly loaded font
            fontMap[fontSize] = FontLoader.createFont(ResourceLocation("afraidofthedark:fonts/targa_ms_hand.ttf"), fontSize)
        }

        // Get the font from the map
        return fontMap[fontSize]!!
    }
}