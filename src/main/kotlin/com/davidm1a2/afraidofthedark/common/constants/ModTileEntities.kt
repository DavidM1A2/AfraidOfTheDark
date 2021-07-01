package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.tileEntity.DarkForestTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.DesertOasisTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.EnariaSpawnerTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.GhastlyEnariaSpawnerTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import net.minecraft.tileentity.TileEntityType

/**
 * A static class containing all of our tile entity references for us
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object ModTileEntities {
    val VOID_CHEST: TileEntityType<*> = TileEntityType
        .Builder
        .create({ VoidChestTileEntity() }, ModBlocks.VOID_CHEST)
        .build(null)
        .setRegistryName(Constants.MOD_ID, "void_chest")
    val DARK_FOREST: TileEntityType<*> = TileEntityType
        .Builder
        .create({ DarkForestTileEntity() }, ModBlocks.DARK_FOREST)
        .build(null)
        .setRegistryName(Constants.MOD_ID, "dark_forest")
    val GHASTLY_ENARIA_SPAWNER: TileEntityType<*> = TileEntityType
        .Builder
        .create({ GhastlyEnariaSpawnerTileEntity() }, ModBlocks.ENARIA_SPAWNER)
        .build(null)
        .setRegistryName(Constants.MOD_ID, "ghastly_enaria_spawner")
    val ENARIA_SPAWNER: TileEntityType<*> = TileEntityType
        .Builder
        .create({ EnariaSpawnerTileEntity() }, ModBlocks.ENARIA_SPAWNER)
        .build(null)
        .setRegistryName(Constants.MOD_ID, "enaria_spawner")
    val DESERT_OASIS: TileEntityType<*> = TileEntityType
        .Builder
        .create({ DesertOasisTileEntity() }, ModBlocks.DESERT_OASIS)
        .build(null)
        .setRegistryName(Constants.MOD_ID, "desert_oasis")
    val ENARIAS_ALTAR: TileEntityType<*> = TileEntityType
        .Builder
        .create({ EnariasAltarTileEntity() }, ModBlocks.ENARIAS_ALTAR)
        .build(null)
        .setRegistryName(Constants.MOD_ID, "enarias_altar")

    // A list of tile entities to register
    val TILE_ENTITY_LIST = arrayOf(
        VOID_CHEST,
        DARK_FOREST,
        GHASTLY_ENARIA_SPAWNER,
        ENARIA_SPAWNER,
        DESERT_OASIS,
        ENARIAS_ALTAR
    )
}