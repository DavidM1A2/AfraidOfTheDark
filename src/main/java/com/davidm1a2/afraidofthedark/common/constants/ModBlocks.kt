package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.block.*
import com.davidm1a2.afraidofthedark.common.block.gravewood.*
import com.davidm1a2.afraidofthedark.common.block.mangrove.*
import com.davidm1a2.afraidofthedark.common.block.sacred_mangrove.*
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityDarkForest
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityEnariaSpawner
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityGhastlyEnariaSpawner
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our block references for us
 */
object ModBlocks {
    val GRAVEWOOD = BlockGravewood()
    val GRAVEWOOD_PLANKS = BlockGravewoodPlanks()
    val GRAVEWOOD_LEAVES = BlockGravewoodLeaves()
    val GRAVEWOOD_HALF_SLAB = BlockGravewoodHalfSlab()
    val GRAVEWOOD_DOUBLE_SLAB = BlockGravewoodDoubleSlab()
    val GRAVEWOOD_STAIRS = BlockGravewoodStairs()
    val GRAVEWOOD_SAPLING = BlockGravewoodSapling()
    val GRAVEWOOD_FENCE = BlockGravewoodFence()
    val GRAVEWOOD_FENCE_GATE = BlockGravewoodFenceGate()
    val GRAVEWOOD_DOOR = BlockGravewoodDoor()

    val MANGROVE = BlockMangrove()
    val MANGROVE_PLANKS = BlockMangrovePlanks()
    val MANGROVE_LEAVES = BlockMangroveLeaves()
    val MANGROVE_HALF_SLAB = BlockMangroveHalfSlab()
    val MANGROVE_DOUBLE_SLAB = BlockMangroveDoubleSlab()
    val MANGROVE_STAIRS = BlockMangroveStairs()
    val MANGROVE_SAPLING = BlockMangroveSapling()
    val MANGROVE_FENCE = BlockMangroveFence()
    val MANGROVE_FENCE_GATE = BlockMangroveFenceGate()
    val MANGROVE_DOOR = BlockMangroveDoor()

    val SACRED_MANGROVE = BlockSacredMangrove()
    val SACRED_MANGROVE_PLANKS = BlockSacredMangrovePlanks()
    val SACRED_MANGROVE_LEAVES = BlockSacredMangroveLeaves()
    val SACRED_MANGROVE_HALF_SLAB = BlockSacredMangroveHalfSlab()
    val SACRED_MANGROVE_DOUBLE_SLAB = BlockSacredMangroveDoubleSlab()
    val SACRED_MANGROVE_STAIRS = BlockSacredMangroveStairs()
    val SACRED_MANGROVE_SAPLING = BlockSacredMangroveSapling()
    val SACRED_MANGROVE_FENCE = BlockSacredMangroveFence()
    val SACRED_MANGROVE_FENCE_GATE = BlockSacredMangroveFenceGate()
    val SACRED_MANGROVE_DOOR = BlockSacredMangroveDoor()

    val METEOR = BlockMeteor()
    val ASTRAL_SILVER_ORE = BlockAstralSilverOre()
    val SUNSTONE_ORE = BlockSunstoneOre()
    val STAR_METAL_ORE = BlockStarMetalOre()
    val IGNEOUS = BlockIgneous()
    val GNOMISH_METAL_STRUT = BlockGnomishMetalStrut()
    val GNOMISH_METAL_PLATE = BlockGnomishMetalPlate()
    val GLOW_STALK = BlockGlowStalk()
    val ELDRITCH_STONE = BlockEldritchStone()
    val ELDRITCH_OBSIDIAN = BlockEldritchObsidian()
    val AMORPHOUS_ELDRITCH_METAL = BlockAmorphousEldritchMetal()
    val VOID_CHEST = BlockVoidChest()
    val VOID_CHEST_PORTAL = BlockVoidChestPortal()
    val DARK_FOREST = BlockDarkForest()
    val ENARIA_SPAWNER = BlockEnariaSpawner()
    val ENARIAS_ALTAR = BlockEnariasAltar()

    // An array containing a list of blocks that AOTD adds
    val BLOCK_LIST = arrayOf(
        GRAVEWOOD,
        GRAVEWOOD_PLANKS,
        GRAVEWOOD_SAPLING,
        GRAVEWOOD_LEAVES,
        GRAVEWOOD_HALF_SLAB,
        GRAVEWOOD_DOUBLE_SLAB,
        GRAVEWOOD_STAIRS,
        GRAVEWOOD_FENCE,
        GRAVEWOOD_FENCE_GATE,
        GRAVEWOOD_DOOR,
        MANGROVE,
        MANGROVE_PLANKS,
        MANGROVE_SAPLING,
        MANGROVE_LEAVES,
        MANGROVE_HALF_SLAB,
        MANGROVE_DOUBLE_SLAB,
        MANGROVE_STAIRS,
        MANGROVE_FENCE,
        MANGROVE_FENCE_GATE,
        MANGROVE_DOOR,
        SACRED_MANGROVE,
        SACRED_MANGROVE_PLANKS,
        SACRED_MANGROVE_SAPLING,
        SACRED_MANGROVE_LEAVES,
        SACRED_MANGROVE_HALF_SLAB,
        SACRED_MANGROVE_DOUBLE_SLAB,
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
        ENARIAS_ALTAR
    )

    // A list of tile entities to register
    val TILE_ENTITY_LIST = arrayOf(
        TileEntityVoidChest::class.java to ResourceLocation(Constants.MOD_ID, "tile_entity_void_chest"),
        TileEntityDarkForest::class.java to ResourceLocation(Constants.MOD_ID, "tile_entity_dark_forest"),
        TileEntityGhastlyEnariaSpawner::class.java to ResourceLocation(
            Constants.MOD_ID,
            "tile_entity_ghastly_enaria_spawner"
        ),
        TileEntityEnariaSpawner::class.java to ResourceLocation(Constants.MOD_ID, "tile_entity_enaria_spawner")
    )
}