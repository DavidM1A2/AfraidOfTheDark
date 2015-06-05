/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.client.gui.ResearchAchieved;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

// Refrences for static final variables
public class Refrence
{
	// The ID of the mod
	public static final String MOD_ID = "AfraidOfTheDark";
	// The mod name
	public static final String MOD_NAME = "Afraid of the Dark";
	// The minecraft version number and version
	public static final String VERSION = "1.8-Alpha 0.1";
	// Refrences to the proxies
	public static final String SERVER_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.common.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.common.proxy.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "com.DavidM1A2.AfraidOfTheDark.client.gui.GuiFactory";
	// Packet ids
	public static final int PACKET_ID_CROSSBOW = 1;
	public static final int PACKET_ID_INSANITY_UPDATE = 2;
	public static final int PACKET_ID_HAS_STARTED_AOTD_UPDATE = 3;
	public static final int PACKET_ID_RESEARCH_UPDATE = 4;
	public static final int PACKET_ID_CREATE_METEOR = 5;
	public static final int PACKET_ID_VITAE_UPDATE = 6;
	public static final int PACKET_ID_ROTATE_PLAYER_UPDATE = 7;
	// Network channel name is the same as the ID
	public static final String NETWORK_CHANNEL_NAME = Refrence.MOD_ID;

	public static ResearchTypes currentlySelected = ResearchTypes.AnUnbreakableCovenant;

	@SideOnly(Side.CLIENT)
	public static CustomFont journalFont;
	@SideOnly(Side.CLIENT)
	public static CustomFont journalTitleFont;

	// Silver weapon damage type and silver tool material
	public static final DamageSource silverWeapon = new DamageSource("silverWeapon");
	public static final ToolMaterial astralSilver = EnumHelper.addToolMaterial("silverTool", 2, 250, 1, 3.0F, 20);
	public static final ToolMaterial igneousTool = EnumHelper.addToolMaterial("igneousTool", 3, 600, 1, 4, 15);
	public static final ToolMaterial starMetalTool = EnumHelper.addToolMaterial("starMetalTool", 3, 600, 1, 4, 15);
	public static final ArmorMaterial igneous = EnumHelper.addArmorMaterial("igneous", "texture", 100, new int[]
	{ 3, 8, 6, 3 }, 20);
	public static final ArmorMaterial starMetal = EnumHelper.addArmorMaterial("starMetal", "texture", 100, new int[]
	{ 3, 8, 6, 3 }, 20);

	public static int[] selectedMeteor = new int[]
	{ -1, -1, -1 };
	public static MeteorTypes watchedMeteorType = null;

	public static ResearchAchieved researchAchievedOverlay;

	// The creative tab
	public static final CreativeTabs AFRAID_OF_THE_DARK = new CreativeTabs(Refrence.MOD_ID.toLowerCase())
	{
		// Icon of the tab is the journal
		@Override
		public Item getTabIconItem()
		{
			return ModItems.journal;
		}
	};
}
