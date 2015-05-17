/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.refrence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.client.gui.ResearchAchieved;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.research.ResearchTypes;

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
	public static final String SERVER_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "com.DavidM1A2.AfraidOfTheDark.client.gui.GuiFactory";
	// Packet ids
	public static final int PACKET_ID_CROSSBOW = 1;
	public static final int PACKET_ID_INSANITY_UPDATE = 2;
	public static final int PACKET_ID_HAS_STARTED_AOTD_UPDATE_CLIENT = 3;
	public static final int PACKET_ID_HAS_STARTED_AOTD_UPDATE_SERVER = 4;
	public static final int PACKET_ID_RESEARCH_UPDATE_CLIENT = 5;
	public static final int PACKET_ID_RESEARCH_UPDATE_SERVER = 6;
	// Network channel name is the same as the ID
	public static final String NETWORK_CHANNEL_NAME = Refrence.MOD_ID;

	public static ResearchTypes currentlySelected = ResearchTypes.AnUnbreakableCovenant;

	@SideOnly(Side.CLIENT)
	public static CustomFont journalFont;
	@SideOnly(Side.CLIENT)
	public static CustomFont journalTitleFont;

	// Silver weapon damage type and silver tool material
	public static final DamageSource silverWeapon = new DamageSource("silverWeapon").setProjectile();
	public static final ToolMaterial silver = EnumHelper.addToolMaterial("silverTool", 2, 250, 1, 4F, 22);
	public static final ArmorMaterial igneous = EnumHelper.addArmorMaterial("igneous", "texture", 100, new int[]
	{ 3, 5, 4, 3 }, 25);

	public static ResearchAchieved researchAchievedOverlay;

	static
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			try
			{
				InputStream textFont = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Refrence.MOD_ID, "fonts/Targa MS Hand.ttf")).getInputStream();
				InputStream titleFont = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Refrence.MOD_ID, "fonts/coolvetica.ttf")).getInputStream();

				journalFont = new CustomFont(textFont, 16);
				journalTitleFont = new CustomFont(titleFont, 26);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

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
