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
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemIronBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemSilverBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.item.ItemIgneousGem;
import com.DavidM1A2.AfraidOfTheDark.item.ItemInsanityControl;
import com.DavidM1A2.AfraidOfTheDark.item.ItemJournal;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSilverIngot;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSilverSword;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSpawnWerewolf;
import com.DavidM1A2.AfraidOfTheDark.item.ItemTelescope;
import com.DavidM1A2.AfraidOfTheDark.item.crossbow.ItemCrossbow;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

@GameRegistry.ObjectHolder(Refrence.MOD_ID)
public class ModItems
{
	// Register mod items
	public static final ItemInsanityControl insanityControl = new ItemInsanityControl();
	public static final ItemJournal journal = new ItemJournal();
	public static final ItemSilverSword silverSword = new ItemSilverSword();
	public static final ItemSilverIngot silverIngot = new ItemSilverIngot();
	public static final ItemCrossbow crossbow = new ItemCrossbow();
	public static final ItemIronBolt ironBolt = new ItemIronBolt();
	public static final ItemSilverBolt silverBolt = new ItemSilverBolt();
	public static final ItemSpawnWerewolf spawnWerewolf = new ItemSpawnWerewolf("Werewolf");
	public static final ItemWoodenBolt woodenBolt = new ItemWoodenBolt();
	public static final IgneousArmor igneousHelmet = new IgneousArmor(Refrence.igneous, 5, 0);
	public static final IgneousArmor igneousChestplate = new IgneousArmor(Refrence.igneous, 5, 1);
	public static final IgneousArmor igneousLeggings = new IgneousArmor(Refrence.igneous, 5, 2);
	public static final IgneousArmor igneousBoots = new IgneousArmor(Refrence.igneous, 5, 3);
	public static final ItemIgneousGem igneousGem = new ItemIgneousGem();
	public static final ItemTelescope telescope = new ItemTelescope();

	public static void initialize(Side side)
	{
		// Register items
		GameRegistry.registerItem(insanityControl, "insanityControl");
		GameRegistry.registerItem(journal, "journal");
		GameRegistry.registerItem(silverSword, "silverSword");
		GameRegistry.registerItem(silverIngot, "silverIngot");
		GameRegistry.registerItem(crossbow, "crossbow");
		GameRegistry.registerItem(ironBolt, "ironBolt");
		GameRegistry.registerItem(silverBolt, "silverBolt");
		GameRegistry.registerItem(spawnWerewolf, "spawnWerewolf");
		GameRegistry.registerItem(woodenBolt, "woodenBolt");
		GameRegistry.registerItem(igneousHelmet, "igneousHelmet");
		GameRegistry.registerItem(igneousChestplate, "igneousChestplate");
		GameRegistry.registerItem(igneousLeggings, "igneousLeggings");
		GameRegistry.registerItem(igneousBoots, "igneousBoots");
		GameRegistry.registerItem(igneousGem, "igneousGem");
		GameRegistry.registerItem(telescope, "telescope");
	}

	public static void initializeRenderers(Side side)
	{
		if (side == Side.CLIENT)
		{
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			renderItem.getItemModelMesher().register(insanityControl, 0, new ModelResourceLocation(Refrence.MOD_ID + ":insanityControl", "inventory"));
			renderItem.getItemModelMesher().register(journal, 0, new ModelResourceLocation(Refrence.MOD_ID + ":journal", "inventory"));
			renderItem.getItemModelMesher().register(silverSword, 0, new ModelResourceLocation(Refrence.MOD_ID + ":silverSword", "inventory"));
			renderItem.getItemModelMesher().register(silverIngot, 0, new ModelResourceLocation(Refrence.MOD_ID + ":silverIngot", "inventory"));
			renderItem.getItemModelMesher().register(crossbow, 0, new ModelResourceLocation(Refrence.MOD_ID + ":crossbow", "inventory"));
			renderItem.getItemModelMesher().register(ironBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":ironBolt", "inventory"));
			renderItem.getItemModelMesher().register(silverBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":silverBolt", "inventory"));
			renderItem.getItemModelMesher().register(woodenBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":woodenBolt", "inventory"));
			renderItem.getItemModelMesher().register(spawnWerewolf, 0, new ModelResourceLocation(Refrence.MOD_ID + ":spawnWerewolf", "inventory"));
			renderItem.getItemModelMesher().register(igneousHelmet, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousHelmet", "inventory"));
			renderItem.getItemModelMesher().register(igneousChestplate, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousChestplate", "inventory"));
			renderItem.getItemModelMesher().register(igneousLeggings, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousLeggings", "inventory"));
			renderItem.getItemModelMesher().register(igneousBoots, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousBoots", "inventory"));
			renderItem.getItemModelMesher().register(igneousGem, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousGem", "inventory"));
			renderItem.getItemModelMesher().register(telescope, 0, new ModelResourceLocation(Refrence.MOD_ID + ":telescope", "inventory"));
		}
	}
}
