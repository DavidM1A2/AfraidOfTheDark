/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemIronBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemSilverBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.ItemWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.item.ItemInsanityControl;
import com.DavidM1A2.AfraidOfTheDark.item.ItemJournal;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSilverIngot;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSilverSword;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSpawnWerewolf;
import com.DavidM1A2.AfraidOfTheDark.item.crossbow.ItemCrossbow;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import cpw.mods.fml.common.registry.GameRegistry;

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

	public static void initialize()
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
	}
}
