package com.davidm1a2.afraidofthedark.client.settings;

import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.FontLoader;
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import com.davidm1a2.afraidofthedark.common.registry.research.Research;
import com.davidm1a2.afraidofthedark.common.spell.Spell;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * A singleton class containing temporary client data to store
 */
public class ClientData
{
    // The single client data instance
    private static final ClientData INSTANCE = new ClientData();

    // A mapping of font size -> font object used to render text
    private Map<Float, TrueTypeFont> fontMap = new HashMap<>();

    // A field that will keep track of which research is currently selected
    private Research lastSelectedResearch = null;

    // A field that will keep track of which spell is currently selected
    private Spell lastSelectedSpell = null;

    /**
     * @return The single instance of the client data object
     */
    public static ClientData getInstance()
    {
        return INSTANCE;
    }

    /**
     * Getter for TargaMSHand font based on a font size. If the object is not yet cached, create it
     *
     * @param fontSize The size of the font to get
     * @return The font object to get
     */
    public TrueTypeFont getTargaMSHandFontSized(float fontSize)
    {
        // If the font map does not contain the size, create that font size and store it
        if (!this.fontMap.containsKey(fontSize))
        // Put the font size with the newly loaded font
        {
            TrueTypeFont font = FontLoader.INSTANCE.createFont(new ResourceLocation("afraidofthedark:fonts/targa_ms_hand.ttf"), fontSize, true);
            this.fontMap.put(fontSize, font);
        }
        // Get the font from the map
        return this.fontMap.get(fontSize);
    }

    /**
     * Sets the last selected research on the ResearchGUI
     *
     * @param research The selected research
     */
    public void setLastSelectedResearch(Research research)
    {
        this.lastSelectedResearch = research;
    }

    /**
     * @return The last selected research on the ResearchGUI
     */
    public Research getLastSelectedResearch()
    {
        return lastSelectedResearch;
    }

    /**
     * Sets the last selected spell on the SpellEditGUI
     *
     * @param lastSelectedSpell The selected spell
     */
    public void setLastSelectedSpell(Spell lastSelectedSpell)
    {
        this.lastSelectedSpell = lastSelectedSpell;
    }

    /**
     * @return The last selected spell on the SpellEditGUI
     */
    public Spell getLastSelectedSpell()
    {
        return lastSelectedSpell;
    }
}
