package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.tileEntity.*
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * A static class containing all of our tile entity references for us
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object ModTileEntities {
    val VOID_CHEST: BlockEntityType<VoidChestTileEntity> = BlockEntityType
        .Builder
        .of(::VoidChestTileEntity, ModBlocks.VOID_CHEST)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "void_chest")
        }
    val CRYPT: BlockEntityType<CryptTileEntity> = BlockEntityType
        .Builder
        .of(::CryptTileEntity, ModBlocks.CRYPT)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "crypt")
        }
    val DARK_FOREST: BlockEntityType<DarkForestTileEntity> = BlockEntityType
        .Builder
        .of(::DarkForestTileEntity, ModBlocks.DARK_FOREST)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "dark_forest")
        }
    val OBSERVATORY: BlockEntityType<ObservatoryTileEntity> = BlockEntityType
        .Builder
        .of(::ObservatoryTileEntity, ModBlocks.OBSERVATORY)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "observatory")
        }
    val WITCH_HUT: BlockEntityType<WitchHutTileEntity> = BlockEntityType
        .Builder
        .of(::WitchHutTileEntity, ModBlocks.WITCH_HUT)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "witch_hut")
        }
    val GHASTLY_ENARIA_SPAWNER: BlockEntityType<GhastlyEnariaSpawnerTileEntity> = BlockEntityType
        .Builder
        .of(::GhastlyEnariaSpawnerTileEntity, ModBlocks.ENARIA_SPAWNER)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "ghastly_enaria_spawner")
        }
    val ENARIA_SPAWNER: BlockEntityType<EnariaSpawnerTileEntity> = BlockEntityType
        .Builder
        .of(::EnariaSpawnerTileEntity, ModBlocks.ENARIA_SPAWNER)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "enaria_spawner")
        }
    val DESERT_OASIS: BlockEntityType<DesertOasisTileEntity> = BlockEntityType
        .Builder
        .of(::DesertOasisTileEntity, ModBlocks.DESERT_OASIS)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "desert_oasis")
        }
    val ENARIAS_ALTAR: BlockEntityType<EnariasAltarTileEntity> = BlockEntityType
        .Builder
        .of(::EnariasAltarTileEntity, ModBlocks.ENARIAS_ALTAR)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "enarias_altar")
        }
    val DROPPED_JOURNAL: BlockEntityType<DroppedJournalTileEntity> = BlockEntityType
        .Builder
        .of(::DroppedJournalTileEntity, ModBlocks.DROPPED_JOURNAL)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "dropped_journal")
        }
    val VITAE_EXTRACTOR: BlockEntityType<VitaeExtractorTileEntity> = BlockEntityType
        .Builder
        .of(::VitaeExtractorTileEntity, ModBlocks.VITAE_EXTRACTOR)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "vitae_extractor")
        }
    val MAGIC_CRYSTAL: BlockEntityType<MagicCrystalTileEntity> = BlockEntityType
        .Builder
        .of(::MagicCrystalTileEntity, ModBlocks.MAGIC_CRYSTAL)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "magic_crystal")
        }
    val SPELL_CRAFTING_TABLE: BlockEntityType<SpellCraftingTableTileEntity> = BlockEntityType
        .Builder
        .of(::SpellCraftingTableTileEntity, ModBlocks.SPELL_CRAFTING_TABLE)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "spell_crafting_table")
        }
    val FROST_PHOENIX_SPAWNER: BlockEntityType<FrostPhoenixSpawnerTileEntity> = BlockEntityType
        .Builder
        .of(::FrostPhoenixSpawnerTileEntity, ModBlocks.FROST_PHOENIX_SPAWNER)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "frost_phoenix_spawner")
        }
    val FEY_LIGHT: BlockEntityType<FeyLightTileEntity> = BlockEntityType
        .Builder
        .of(::FeyLightTileEntity, ModBlocks.FEY_LIGHT)
        .build(null).apply {
            setRegistryName(Constants.MOD_ID, "fey_light")
        }

    // A list of tile entities to register
    val TILE_ENTITY_LIST = arrayOf(
        VOID_CHEST,
        CRYPT,
        DARK_FOREST,
        WITCH_HUT,
        GHASTLY_ENARIA_SPAWNER,
        ENARIA_SPAWNER,
        DESERT_OASIS,
        ENARIAS_ALTAR,
        OBSERVATORY,
        DROPPED_JOURNAL,
        VITAE_EXTRACTOR,
        MAGIC_CRYSTAL,
        SPELL_CRAFTING_TABLE,
        FROST_PHOENIX_SPAWNER,
        FEY_LIGHT
    )
}