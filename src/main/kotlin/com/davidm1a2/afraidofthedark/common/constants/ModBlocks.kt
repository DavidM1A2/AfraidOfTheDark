package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.block.*
import com.davidm1a2.afraidofthedark.common.block.gravewood.*
import com.davidm1a2.afraidofthedark.common.block.mangrove.*
import com.davidm1a2.afraidofthedark.common.block.sacred_mangrove.*
import net.minecraft.block.Block

/**
 * A static class containing all of our block references for us
 */
object ModBlocks {
    val GRAVEWOOD = GravewoodBlock()
    val GRAVEWOOD_PLANKS = GravewoodPlanksBlock()
    val GRAVEWOOD_LEAVES = GravewoodLeavesBlock()
    val GRAVEWOOD_SLAB = GravewoodSlabBlock()
    val GRAVEWOOD_STAIRS = GravewoodStairsBlock()
    val GRAVEWOOD_SAPLING = GravewoodSaplingBlock()
    val GRAVEWOOD_FENCE = GravewoodFenceBlock()
    val GRAVEWOOD_FENCE_GATE = GravewoodFenceGateBlock()
    val GRAVEWOOD_DOOR = GravewoodDoorBlock()

    val MANGROVE = MangroveBlock()
    val MANGROVE_PLANKS = MangrovePlanksBlock()
    val MANGROVE_LEAVES = MangroveLeavesBlock()
    val MANGROVE_SLAB = MangroveSlabBlock()
    val MANGROVE_STAIRS = MangroveStairsBlock()
    val MANGROVE_SAPLING = MangroveSaplingBlock()
    val MANGROVE_FENCE = MangroveFenceBlock()
    val MANGROVE_FENCE_GATE = MangroveFenceGateBlock()
    val MANGROVE_DOOR = MangroveDoorBlock()

    val SACRED_MANGROVE = SacredMangroveBlock()
    val SACRED_MANGROVE_PLANKS = SacredMangrovePlanksBlock()
    val SACRED_MANGROVE_LEAVES = SacredMangroveLeavesBlock()
    val SACRED_MANGROVE_SLAB = SacredMangroveSlabBlock()
    val SACRED_MANGROVE_STAIRS = SacredMangroveStairsBlock()
    val SACRED_MANGROVE_SAPLING = SacredMangroveSaplingBlock()
    val SACRED_MANGROVE_FENCE = SacredMangroveFenceBlock()
    val SACRED_MANGROVE_FENCE_GATE = SacredMangroveFenceGateBlock()
    val SACRED_MANGROVE_DOOR = SacredMangroveDoorBlock()

    val METEOR = MeteorBlock()
    val ASTRAL_SILVER_ORE = AstralSilverOreBlock()
    val SUNSTONE_ORE = SunstoneOreBlock()
    val STAR_METAL_ORE = StarMetalOreBlock()
    val IGNEOUS = IgneousBlock()
    val GNOMISH_METAL_STRUT = GnomishMetalStrutBlock()
    val GNOMISH_METAL_PLATE = GnomishMetalPlateBlock()
    val GLOW_STALK = GlowStalkBlock()
    val ELDRITCH_STONE = EldritchStoneBlock()
    val ELDRITCH_OBSIDIAN = EldritchObsidianBlock()
    val AMORPHOUS_ELDRITCH_METAL = AmorphousEldritchMetalBlock()
    val VOID_CHEST = VoidChestBlock()
    val VOID_CHEST_PORTAL = VoidChestPortalBlock()
    val DARK_FOREST = DarkForestBlock()
    val ENARIA_SPAWNER = EnariaSpawnerBlock()
    val ENARIAS_ALTAR = EnariasAltarBlock()
    val IMBUED_CACTUS = ImbuedCactusBlock()
    val IMBUED_CACTUS_BLOSSOM = ImbuedCactusBlossomBlock()
    val DESERT_OASIS = DesertOasisBlock()
    val LENS_CUTTER = LensCutterBlock()

    // An array containing a list of blocks that AOTD adds
    val BLOCK_LIST = arrayOf<Block>(
        GRAVEWOOD,
        GRAVEWOOD_PLANKS,
        GRAVEWOOD_SAPLING,
        GRAVEWOOD_LEAVES,
        GRAVEWOOD_SLAB,
        GRAVEWOOD_STAIRS,
        GRAVEWOOD_FENCE,
        GRAVEWOOD_FENCE_GATE,
        GRAVEWOOD_DOOR,
        MANGROVE,
        MANGROVE_PLANKS,
        MANGROVE_SAPLING,
        MANGROVE_LEAVES,
        MANGROVE_SLAB,
        MANGROVE_STAIRS,
        MANGROVE_FENCE,
        MANGROVE_FENCE_GATE,
        MANGROVE_DOOR,
        SACRED_MANGROVE,
        SACRED_MANGROVE_PLANKS,
        SACRED_MANGROVE_SAPLING,
        SACRED_MANGROVE_LEAVES,
        SACRED_MANGROVE_SLAB,
        SACRED_MANGROVE_STAIRS,
        SACRED_MANGROVE_FENCE,
        SACRED_MANGROVE_FENCE_GATE,
        SACRED_MANGROVE_DOOR,
        METEOR,
        ASTRAL_SILVER_ORE,
        ELDRITCH_STONE,
        ELDRITCH_OBSIDIAN,
        AMORPHOUS_ELDRITCH_METAL,
        VOID_CHEST,
        VOID_CHEST_PORTAL,
        DARK_FOREST,
        ENARIA_SPAWNER,
        SUNSTONE_ORE,
        STAR_METAL_ORE,
        IGNEOUS,
        GNOMISH_METAL_STRUT,
        GNOMISH_METAL_PLATE,
        GLOW_STALK,
        ENARIAS_ALTAR,
        IMBUED_CACTUS,
        IMBUED_CACTUS_BLOSSOM,
        DESERT_OASIS,
        LENS_CUTTER
    )
}