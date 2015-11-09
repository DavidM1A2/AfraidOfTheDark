/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
	// Various entity IDs
	public static final int wereWolfID = 0;
	public static final int ironBoltID = 1;
	public static final int silverBoltID = 2;
	public static final int woodenBoltID = 3;
	public static final int deeeSyft = 4;
	public static final int igneousBoltID = 5;
	public static final int starMetalBoltID = 6;
	public static final int enchantedSkeletonID = 7;
	public static final int enariaID = 8;
	public static final int splinterDroneID = 9;

	public static void intialize()
	{
		// register bolt entities and the werewolf
		EntityRegistry.registerModEntity(EntityWerewolf.class, "werewolf", ModEntities.wereWolfID, Refrence.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(EntityDeeeSyft.class, "deeeSyft", ModEntities.deeeSyft, Refrence.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(EntityEnchantedSkeleton.class, "enchantedSkeleton", ModEntities.enchantedSkeletonID, Refrence.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(EntityEnaria.class, "enaria", ModEntities.enariaID, Refrence.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(EntitySplinterDrone.class, "splinterDrone", ModEntities.splinterDroneID, Refrence.MOD_ID, 50, 1, true);

		EntityRegistry.registerModEntity(EntityIronBolt.class, "ironBolt", ModEntities.ironBoltID, Refrence.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(EntitySilverBolt.class, "silverBolt", ModEntities.silverBoltID, Refrence.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(EntityWoodenBolt.class, "woodenBolt", ModEntities.woodenBoltID, Refrence.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(EntityIgneousBolt.class, "igneousBolt", ModEntities.igneousBoltID, Refrence.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(EntityStarMetalBolt.class, "starMetalBolt", ModEntities.starMetalBoltID, Refrence.MOD_ID, 50, 10, true);

		// Allow the werewolf to rarely spawn in all biomes
		for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++)
		{
			if (BiomeGenBase.getBiomeGenArray()[i] != null)
			{
				EntityRegistry.addSpawn(EntityWerewolf.class, 2, 1, 1, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
			}
		}
		// Higher chance to spawn in erie biomes
		EntityRegistry.addSpawn(EntityWerewolf.class, 30, 2, 2, EnumCreatureType.MONSTER, ModBiomes.erieForest);
		EntityRegistry.addSpawn(EntityDeeeSyft.class, 4, 1, 3, EnumCreatureType.MONSTER, ModBiomes.erieForest);
	}
}