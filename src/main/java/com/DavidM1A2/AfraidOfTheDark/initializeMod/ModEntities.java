package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities 
{
	public static final int wereWolfID = 0;
	public static final int ironBoltID = 1;
	public static final int silverBoltID = 2;
	public static final int woodenBoltID = 3;
	
	public static void intialize()
	{
		EntityRegistry.registerModEntity(EntityWereWolf.class, "Werewolf", wereWolfID, Refrence.MOD_ID, 50, 1, true);		
		EntityRegistry.registerModEntity(EntityIronBolt.class, "ironBolt", ironBoltID, Refrence.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(EntitySilverBolt.class, "silverBolt", silverBoltID, Refrence.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(EntityWoodenBolt.class, "woodenBolt", woodenBoltID, Refrence.MOD_ID, 50, 10, true);
		
		for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++)
		{
			if (BiomeGenBase.getBiomeGenArray()[i] != null)
			{
				EntityRegistry.addSpawn(EntityWereWolf.class, 2, 1, 1, EnumCreatureType.monster, BiomeGenBase.getBiomeGenArray()[i]);
			}
		}
		EntityRegistry.addSpawn(EntityWereWolf.class, 30, 2, 2, EnumCreatureType.monster, ModBiomes.erieForest);
	}
}