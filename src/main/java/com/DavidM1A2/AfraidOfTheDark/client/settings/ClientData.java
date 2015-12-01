/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.settings;

import com.DavidM1A2.AfraidOfTheDark.client.gui.ResearchAchieved;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public final class ClientData
{
	public static ResearchTypes currentlySelected = ResearchTypes.AnUnbreakableCovenant;

	public static int[] selectedMeteor = new int[]
	{ -1, -1, -1 };
	public static MeteorTypes watchedMeteorType = null;

	public static ResearchAchieved researchAchievedOverlay;

	public static int currentBloodStainedJournalX = 0;
	public static int currentBloodStainedJournalY = 0;
}
