/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.settings;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.client.gui.ResearchAchieved;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDCrossbowBoltTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.CustomFont;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public final class ClientData
{
	public static ResearchTypes currentlySelected = ResearchTypes.AnUnbreakableCovenant;

	@SideOnly(Side.CLIENT)
	public static CustomFont journalFont;
	@SideOnly(Side.CLIENT)
	public static CustomFont journalTitleFont;

	public static AOTDCrossbowBoltTypes currentlySelectedBolt = AOTDCrossbowBoltTypes.iron;

	public static int[] selectedMeteor = new int[]
	{ -1, -1, -1 };
	public static MeteorTypes watchedMeteorType = null;

	public static ResearchAchieved researchAchievedOverlay;
}
