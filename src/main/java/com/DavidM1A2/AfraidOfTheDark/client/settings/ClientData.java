/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.settings;

import java.util.HashMap;
import java.util.Map;

import com.DavidM1A2.AfraidOfTheDark.client.gui.ResearchAchieved;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.FontLoader;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

import net.minecraft.util.ResourceLocation;

public final class ClientData
{
	private static Map<Float, TrueTypeFont> fontMap = new HashMap<Float, TrueTypeFont>();

	public static ResearchTypes currentlySelected = ResearchTypes.AnUnbreakableCovenant;

	public static int[] selectedMeteor = new int[]
	{ -1, -1, -1 };
	public static MeteorTypes watchedMeteorType = null;

	public static ResearchAchieved researchAchievedOverlay;

	public static int currentBloodStainedJournalX = 0;
	public static int currentBloodStainedJournalY = 0;

	public static TrueTypeFont getTargaMSHandFontSized(float fontSize)
	{
		if (!ClientData.fontMap.containsKey(fontSize))
			ClientData.fontMap.put(fontSize, FontLoader.createFont(new ResourceLocation("afraidofthedark:fonts/Targa MS Hand.ttf"), fontSize, true));
		return ClientData.fontMap.get(fontSize);
	}
}
