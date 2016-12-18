/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EntityArtwork.EntityArtwork;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDroneProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.AOE.EntityAOE;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.myself.EntityMyself;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectileDive;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
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
	public static final int splinterDroneProjectileID = 10;
	public static final int spellProjectileID = 11;
	public static final int spellProjectileDiveID = 12;
	public static final int spellMyselfID = 13;
	public static final int spellAOEID = 14;
	public static final int ghastlyEnariaID = 15;
	public static final int artworkID = 16;
	public static final int spellExtraEffectsID = 17;

	public static void intialize()
	{
		// register entities
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "werewolf"), EntityWerewolf.class, "werewolf", ModEntities.wereWolfID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "deeeSyft"), EntityDeeeSyft.class, "deeeSyft", ModEntities.deeeSyft, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "enchantedSkeleton"), EntityEnchantedSkeleton.class, "enchantedSkeleton", ModEntities.enchantedSkeletonID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "enaria"), EntityEnaria.class, "enaria", ModEntities.enariaID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "splinterDrone"), EntitySplinterDrone.class, "splinterDrone", ModEntities.splinterDroneID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "splinterDroneProjectile"), EntitySplinterDroneProjectile.class, "splinterDroneProjectile", ModEntities.splinterDroneProjectileID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "ghastlyEnaria"), EntityGhastlyEnaria.class, "ghastlyEnaria", ModEntities.ghastlyEnariaID, Reference.MOD_ID, 500, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "artwork"), EntityArtwork.class, "artwork", ModEntities.artworkID, Reference.MOD_ID, 160, Integer.MAX_VALUE, false);

		EntityRegistry.registerEgg(new ResourceLocation(Reference.MOD_ID, "werewolf"), 0x3B170B, 0x181907);
		EntityRegistry.registerEgg(new ResourceLocation(Reference.MOD_ID, "splinterDrone"), 0xcc6600, 0x63300);
		EntityRegistry.registerEgg(new ResourceLocation(Reference.MOD_ID, "enchantedSkeleton"), 0x996600, 0xe69900);
		EntityRegistry.registerEgg(new ResourceLocation(Reference.MOD_ID, "deeeSyft"), 0x0086b3, 0x00bfff);

		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "ironBolt"), EntityIronBolt.class, "ironBolt", ModEntities.ironBoltID, Reference.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "silverBolt"), EntitySilverBolt.class, "silverBolt", ModEntities.silverBoltID, Reference.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "woodenBolt"), EntityWoodenBolt.class, "woodenBolt", ModEntities.woodenBoltID, Reference.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "igneousBolt"), EntityIgneousBolt.class, "igneousBolt", ModEntities.igneousBoltID, Reference.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "starMetalBolt"), EntityStarMetalBolt.class, "starMetalBolt", ModEntities.starMetalBoltID, Reference.MOD_ID, 50, 10, true);

		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spellProjectile"), EntitySpellProjectile.class, "spellProjectile", ModEntities.spellProjectileID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spellProjectileDive"), EntitySpellProjectileDive.class, "spellProjectileDive", ModEntities.spellProjectileDiveID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spellMyself"), EntityMyself.class, "spellMyself", ModEntities.spellMyselfID, Reference.MOD_ID, 0, 1, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spellAOE"), EntityAOE.class, "spellAOE", ModEntities.spellAOEID, Reference.MOD_ID, 0, 1, false);

		// Allow the werewolf to rarely spawn in all biomes
		for (BiomeType type : BiomeType.values())
			for (BiomeEntry biome : BiomeManager.getBiomes(type))
				if (biome != null && Biome.getIdForBiome(biome.biome) != AOTDDimensions.Nightmare.getWorldID())
					EntityRegistry.addSpawn(EntityWerewolf.class, 2, 1, 1, EnumCreatureType.MONSTER, biome.biome);

		// Higher chance to spawn in erie biomes
		EntityRegistry.addSpawn(EntityWerewolf.class, 30, 2, 2, EnumCreatureType.MONSTER, ModBiomes.erieForest);
		EntityRegistry.addSpawn(EntityDeeeSyft.class, 4, 1, 3, EnumCreatureType.MONSTER, ModBiomes.erieForest);
	}
}