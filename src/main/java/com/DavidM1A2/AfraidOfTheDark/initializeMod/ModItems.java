/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.armor.IgneousArmor;
import com.DavidM1A2.AfraidOfTheDark.armor.StarMetalArmor;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemIronBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemSilverBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.item.ItemIgneousGem;
import com.DavidM1A2.AfraidOfTheDark.item.ItemIgneousSword;
import com.DavidM1A2.AfraidOfTheDark.item.ItemInsanityControl;
import com.DavidM1A2.AfraidOfTheDark.item.ItemJournal;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSextant;
import com.DavidM1A2.AfraidOfTheDark.item.ItemAstralSilverIngot;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSilverSword;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSpawnWerewolf;
import com.DavidM1A2.AfraidOfTheDark.item.ItemStarMetal;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSunstone;
import com.DavidM1A2.AfraidOfTheDark.item.ItemTelescope;
import com.DavidM1A2.AfraidOfTheDark.item.ItemTrollPole;
import com.DavidM1A2.AfraidOfTheDark.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.item.crossbow.ItemCrossbow;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

@GameRegistry.ObjectHolder(Refrence.MOD_ID)
public class ModItems
{
	// Register mod items
	public static final ItemInsanityControl insanityControl = new ItemInsanityControl();
	public static final ItemJournal journal = new ItemJournal();
	public static final ItemSilverSword silverSword = new ItemSilverSword();
	public static final ItemIgneousSword igneousSword = new ItemIgneousSword();
	public static final ItemAstralSilverIngot astralSilverIngot = new ItemAstralSilverIngot();
	public static final ItemCrossbow crossbow = new ItemCrossbow();
	public static final ItemIronBolt ironBolt = new ItemIronBolt();
	public static final ItemSilverBolt silverBolt = new ItemSilverBolt();
	public static final ItemSpawnWerewolf spawnWerewolf = new ItemSpawnWerewolf("Werewolf");
	public static final ItemWoodenBolt woodenBolt = new ItemWoodenBolt();
	public static final IgneousArmor igneousHelmet = new IgneousArmor(Refrence.igneous, 5, 0);
	public static final IgneousArmor igneousChestplate = new IgneousArmor(Refrence.igneous, 5, 1);
	public static final IgneousArmor igneousLeggings = new IgneousArmor(Refrence.igneous, 5, 2);
	public static final IgneousArmor igneousBoots = new IgneousArmor(Refrence.igneous, 5, 3);
	public static final StarMetalArmor starMetalHelmet = new StarMetalArmor(Refrence.starMetal, 5, 0);
	public static final StarMetalArmor starMetalChestplate = new StarMetalArmor(Refrence.starMetal, 5, 1);
	public static final StarMetalArmor starMetalLeggings = new StarMetalArmor(Refrence.starMetal, 5, 2);
	public static final StarMetalArmor starMetalBoots = new StarMetalArmor(Refrence.starMetal, 5, 3);
	public static final ItemIgneousGem igneousGem = new ItemIgneousGem();
	public static final ItemTelescope telescope = new ItemTelescope();
	public static final ItemSextant sextant = new ItemSextant();
	public static final ItemSunstone sunstoneIngot = new ItemSunstone();
	public static final ItemStarMetal starMetalIngot = new ItemStarMetal();
	public static final ItemTrollPole trollPole = new ItemTrollPole();
	public static final ItemVitaeLantern vitaeLantern = new ItemVitaeLantern(); 

	public static void initialize(final Side side)
	{
		// Register items
		GameRegistry.registerItem(ModItems.insanityControl, "insanityControl");
		GameRegistry.registerItem(ModItems.journal, "journal");
		GameRegistry.registerItem(ModItems.silverSword, "silverSword");
		GameRegistry.registerItem(ModItems.igneousSword, "igneousSword");
		GameRegistry.registerItem(ModItems.astralSilverIngot, "astralSilverIngot");
		GameRegistry.registerItem(ModItems.crossbow, "crossbow");
		GameRegistry.registerItem(ModItems.ironBolt, "ironBolt");
		GameRegistry.registerItem(ModItems.silverBolt, "silverBolt");
		GameRegistry.registerItem(ModItems.spawnWerewolf, "spawnWerewolf");
		GameRegistry.registerItem(ModItems.woodenBolt, "woodenBolt");
		GameRegistry.registerItem(ModItems.igneousHelmet, "igneousHelmet");
		GameRegistry.registerItem(ModItems.igneousChestplate, "igneousChestplate");
		GameRegistry.registerItem(ModItems.igneousLeggings, "igneousLeggings");
		GameRegistry.registerItem(ModItems.igneousBoots, "igneousBoots");
		GameRegistry.registerItem(ModItems.igneousGem, "igneousGem");
		GameRegistry.registerItem(ModItems.starMetalHelmet, "starMetalHelmet");
		GameRegistry.registerItem(ModItems.starMetalChestplate, "starMetalChestplate");
		GameRegistry.registerItem(ModItems.starMetalLeggings, "starMetalLeggings");
		GameRegistry.registerItem(ModItems.starMetalBoots, "starMetalBoots");
		GameRegistry.registerItem(ModItems.telescope, "telescope");
		GameRegistry.registerItem(ModItems.sextant, "sextant");
		GameRegistry.registerItem(ModItems.sunstoneIngot, "sunstoneIngot");
		GameRegistry.registerItem(ModItems.starMetalIngot, "starMetalIngot");
		GameRegistry.registerItem(ModItems.trollPole, "trollPole");
		GameRegistry.registerItem(ModItems.vitaeLantern, "vitaeLantern");
	}

	public static void initializeRenderers(final Side side)
	{
		if (side == Side.CLIENT)
		{
			final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			renderItem.getItemModelMesher().register(ModItems.insanityControl, 0, new ModelResourceLocation(Refrence.MOD_ID + ":insanityControl", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.journal, 0, new ModelResourceLocation(Refrence.MOD_ID + ":journal", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.silverSword, 0, new ModelResourceLocation(Refrence.MOD_ID + ":silverSword", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.igneousSword, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousSword", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.astralSilverIngot, 0, new ModelResourceLocation(Refrence.MOD_ID + ":astralSilverIngot", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.crossbow, 0, new ModelResourceLocation(Refrence.MOD_ID + ":crossbow", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.ironBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":ironBolt", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.silverBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":silverBolt", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.woodenBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":woodenBolt", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.spawnWerewolf, 0, new ModelResourceLocation(Refrence.MOD_ID + ":spawnWerewolf", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.igneousHelmet, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousHelmet", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.igneousChestplate, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousChestplate", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.igneousLeggings, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousLeggings", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.igneousBoots, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousBoots", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.igneousGem, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousGem", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.starMetalHelmet, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalHelmet", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.starMetalChestplate, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalChestplate", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.starMetalLeggings, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalLeggings", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.starMetalBoots, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalBoots", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.telescope, 0, new ModelResourceLocation(Refrence.MOD_ID + ":telescope", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.sextant, 0, new ModelResourceLocation(Refrence.MOD_ID + ":sextant", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.sunstoneIngot, 0, new ModelResourceLocation(Refrence.MOD_ID + ":sunstoneIngot", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.starMetalIngot, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalIngot", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.trollPole, 0, new ModelResourceLocation(Refrence.MOD_ID + ":trollPole", "inventory"));
			renderItem.getItemModelMesher().register(ModItems.vitaeLantern, 0, new ModelResourceLocation(Refrence.MOD_ID + ":vitaeLantern", "inventory"));
		}
	}
}
