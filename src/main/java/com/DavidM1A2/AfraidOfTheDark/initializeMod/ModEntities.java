/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class ModEntities
{
	// Various entity IDs
	public static final int wereWolfID = 0;
	public static final int ironBoltID = 1;
	public static final int silverBoltID = 2;
	public static final int woodenBoltID = 3;

	public static void intialize()
	{
		// register bolt entities and the werewolf
		EntityRegistry.registerModEntity(EntityWereWolf.class, "Werewolf", ModEntities.wereWolfID, Refrence.MOD_ID, 50, 1, true);

		EntityRegistry.registerModEntity(EntityIronBolt.class, "ironBolt", ModEntities.ironBoltID, Refrence.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(EntitySilverBolt.class, "silverBolt", ModEntities.silverBoltID, Refrence.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(EntityWoodenBolt.class, "woodenBolt", ModEntities.woodenBoltID, Refrence.MOD_ID, 50, 10, true);

		// Allow the werewolf to rarely spawn in all biomes
		for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++)
		{
			if (BiomeGenBase.getBiomeGenArray()[i] != null)
			{
				EntityRegistry.addSpawn(EntityWereWolf.class, 2, 1, 1, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
			}
		}
		// Higher chance to spawn in erie biomes
		EntityRegistry.addSpawn(EntityWereWolf.class, 30, 2, 2, EnumCreatureType.MONSTER, ModBiomes.erieForest);
	}
}