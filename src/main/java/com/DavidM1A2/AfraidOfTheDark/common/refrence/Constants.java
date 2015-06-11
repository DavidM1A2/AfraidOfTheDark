package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

public final class Constants
{
	public static final class Packets
	{
		// Packet ids
		public static final int PACKET_ID_CROSSBOW = 1;
		public static final int PACKET_ID_INSANITY_UPDATE = 2;
		public static final int PACKET_ID_HAS_STARTED_AOTD_UPDATE = 3;
		public static final int PACKET_ID_RESEARCH_UPDATE = 4;
		public static final int PACKET_ID_CREATE_METEOR = 5;
		public static final int PACKET_ID_VITAE_UPDATE = 6;
		public static final int PACKET_ID_ROTATE_PLAYER_UPDATE = 7;
	}

	public static final class AOTDToolMaterials
	{
		public static final ToolMaterial astralSilver = EnumHelper.addToolMaterial("silverTool", 2, 250, 1, 3.0F, 20);
		public static final ToolMaterial igneousTool = EnumHelper.addToolMaterial("igneousTool", 3, 600, 1, 5, 15);
		public static final ToolMaterial starMetalTool = EnumHelper.addToolMaterial("starMetalTool", 3, 600, 1, 4, 15);
	}

	public static final class AOTDArmorMaterials
	{
		public static final ArmorMaterial igneous = EnumHelper.addArmorMaterial("igneous", "texture", 100, new int[]
		{ 3, 8, 6, 3 }, 20);
		public static final ArmorMaterial starMetal = EnumHelper.addArmorMaterial("starMetal", "texture", 100, new int[]
		{ 3, 8, 6, 3 }, 20);
	}

	public static final class AOTDDamageSources
	{
		// Silver weapon damage type and silver tool material
		public static final DamageSource silverWeapon = new DamageSource("silverWeapon");
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
