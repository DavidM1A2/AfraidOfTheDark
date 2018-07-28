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
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "deee_syft"), EntityDeeeSyft.class, "deee_syft", ModEntities.deeeSyft, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "enchanted_skeleton"), EntityEnchantedSkeleton.class, "enchanted_skeleton", ModEntities.enchantedSkeletonID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "enaria"), EntityEnaria.class, "enaria", ModEntities.enariaID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "splinter_drone"), EntitySplinterDrone.class, "splinter_drone", ModEntities.splinterDroneID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "splinter_drone_projectile"), EntitySplinterDroneProjectile.class, "splinter_drone_projectile", ModEntities.splinterDroneProjectileID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "ghastly_enaria"), EntityGhastlyEnaria.class, "ghastly_enaria", ModEntities.ghastlyEnariaID, Reference.MOD_ID, 500, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "artwork"), EntityArtwork.class, "artwork", ModEntities.artworkID, Reference.MOD_ID, 160, Integer.MAX_VALUE, false);

		EntityRegistry.registerEgg(new ResourceLocation(Reference.MOD_ID, "werewolf"), 0x3B170B, 0x181907);
		EntityRegistry.registerEgg(new ResourceLocation(Reference.MOD_ID, "splinter_drone"), 0xcc6600, 0x63300);
		EntityRegistry.registerEgg(new ResourceLocation(Reference.MOD_ID, "enchanted_skeleton"), 0x996600, 0xe69900);
		EntityRegistry.registerEgg(new ResourceLocation(Reference.MOD_ID, "deee_syft"), 0x0086b3, 0x00bfff);

		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "iron_bolt"), EntityIronBolt.class, "iron_bolt", ModEntities.ironBoltID, Reference.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "silver_bolt"), EntitySilverBolt.class, "silver_bolt", ModEntities.silverBoltID, Reference.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "wooden_bolt"), EntityWoodenBolt.class, "wooden_bolt", ModEntities.woodenBoltID, Reference.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "igneous_bolt"), EntityIgneousBolt.class, "igneous_bolt", ModEntities.igneousBoltID, Reference.MOD_ID, 50, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "star_metal_bolt"), EntityStarMetalBolt.class, "star_metal_bolt", ModEntities.starMetalBoltID, Reference.MOD_ID, 50, 10, true);

		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spell_projectile"), EntitySpellProjectile.class, "spell_projectile", ModEntities.spellProjectileID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spell_projectile_dive"), EntitySpellProjectileDive.class, "spell_projectile_dive", ModEntities.spellProjectileDiveID, Reference.MOD_ID, 50, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spell_myself"), EntityMyself.class, "spell_myself", ModEntities.spellMyselfID, Reference.MOD_ID, 0, 1, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spell_AOE"), EntityAOE.class, "spell_AOE", ModEntities.spellAOEID, Reference.MOD_ID, 0, 1, false);

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