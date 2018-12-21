package com.DavidM1A2.afraidofthedark.client.settings;

import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.FontLoader;
import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import net.minecraft.client.gui.FontRenderer;
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
			this.fontMap.put(fontSize, FontLoader.createFont(new ResourceLocation("afraidofthedark:fonts/targa_ms_hand.ttf"), fontSize, true));
		// Get the font from the map
		return this.fontMap.get(fontSize);
	}

	/**
	 * @return The single instance of the client data object
	 */
	public static ClientData getInstance()
	{
		return INSTANCE;
	}
}
