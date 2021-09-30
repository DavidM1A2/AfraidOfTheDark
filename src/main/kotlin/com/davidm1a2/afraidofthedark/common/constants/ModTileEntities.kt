package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.tileEntity.DarkForestTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.DesertOasisTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.DroppedJournalTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.EnariaSpawnerTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.GhastlyEnariaSpawnerTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.ObservatoryTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.VoidObeliskTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.WitchHutTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import net.minecraft.tileentity.TileEntityType

/**
 * A static class containing all of our tile entity references for us
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object ModTileEntities {
    val VOID_CHEST: TileEntityType<VoidChestTileEntity> = TileEntityType
        .Builder
        .of({ VoidChestTileEntity() }, ModBlocks.VOID_CHEST)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "void_chest")
        }
    val DARK_FOREST: TileEntityType<DarkForestTileEntity> = TileEntityType
        .Builder
        .of({ DarkForestTileEntity() }, ModBlocks.DARK_FOREST)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "dark_forest")
        }
    val OBSERVATORY: TileEntityType<ObservatoryTileEntity> = TileEntityType
        .Builder
        .of({ ObservatoryTileEntity() }, ModBlocks.OBSERVATORY)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "observatory")
        }
    val VOID_OBELISK: TileEntityType<VoidObeliskTileEntity> = TileEntityType
        .Builder
        .of({ VoidObeliskTileEntity() }, ModBlocks.VOID_OBELISK)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "void_obelisk")
        }
    val WITCH_HUT: TileEntityType<WitchHutTileEntity> = TileEntityType
        .Builder
        .of({ WitchHutTileEntity() }, ModBlocks.WITCH_HUT)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "witch_hut")
        }
    val GHASTLY_ENARIA_SPAWNER: TileEntityType<GhastlyEnariaSpawnerTileEntity> = TileEntityType
        .Builder
        .of({ GhastlyEnariaSpawnerTileEntity() }, ModBlocks.ENARIA_SPAWNER)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "ghastly_enaria_spawner")
        }
    val ENARIA_SPAWNER: TileEntityType<EnariaSpawnerTileEntity> = TileEntityType
        .Builder
        .of({ EnariaSpawnerTileEntity() }, ModBlocks.ENARIA_SPAWNER)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "enaria_spawner")
        }
    val DESERT_OASIS: TileEntityType<DesertOasisTileEntity> = TileEntityType
        .Builder
        .of({ DesertOasisTileEntity() }, ModBlocks.DESERT_OASIS)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "desert_oasis")
        }
    val ENARIAS_ALTAR: TileEntityType<EnariasAltarTileEntity> = TileEntityType
        .Builder
        .of({ EnariasAltarTileEntity() }, ModBlocks.ENARIAS_ALTAR)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "enarias_altar")
        }
    val DROPPED_JOURNAL: TileEntityType<DroppedJournalTileEntity> = TileEntityType
        .Builder
        .of({ DroppedJournalTileEntity() }, ModBlocks.DROPPED_JOURNAL)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "dropped_journal")
        }

    // A list of tile entities to register
    val TILE_ENTITY_LIST = arrayOf(
        VOID_CHEST,
        DARK_FOREST,
        WITCH_HUT,
        VOID_OBELISK,
        GHASTLY_ENARIA_SPAWNER,
        ENARIA_SPAWNER,
        DESERT_OASIS,
        ENARIAS_ALTAR,
        OBSERVATORY,
        DROPPED_JOURNAL
    )
}