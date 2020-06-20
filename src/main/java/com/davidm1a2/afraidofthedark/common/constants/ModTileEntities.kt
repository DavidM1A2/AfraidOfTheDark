package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.tileEntity.*
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.TileEntityEnariasAltar
import net.minecraft.tileentity.TileEntityType

/**
 * A static class containing all of our tile entity references for us
 */
object ModTileEntities {
    val VOID_CHEST = TileEntityType
        .Builder
        .create { TileEntityVoidChest() }
        .build(null)
        .setRegistryName(Constants.MOD_ID, "void_chest")
    val DARK_FOREST = TileEntityType
        .Builder
        .create { TileEntityDarkForest() }
        .build(null)
        .setRegistryName(Constants.MOD_ID, "dark_forest")
    val GHASTLY_ENARIA_SPAWNER = TileEntityType
        .Builder
        .create { TileEntityGhastlyEnariaSpawner() }
        .build(null)
        .setRegistryName(Constants.MOD_ID, "ghastly_enaria_spawner")
    val ENARIA_SPAWNER = TileEntityType
        .Builder
        .create { TileEntityEnariaSpawner() }
        .build(null)
        .setRegistryName(Constants.MOD_ID, "enaria_spawner")
    val DESERT_OASIS = TileEntityType
        .Builder
        .create { TileEntityDesertOasis() }
        .build(null)
        .setRegistryName(Constants.MOD_ID, "desert_oasis")
    val SPELL_ALTAR = TileEntityType
        .Builder
        .create { TileEntitySpellAltar() }
        .build(null)
        .setRegistryName(Constants.MOD_ID, "spell_altar")
    val ENARIAS_ALTAR = TileEntityType
        .Builder
        .create { TileEntityEnariasAltar() }
        .build(null)
        .setRegistryName(Constants.MOD_ID, "enarias_altar")

    // A list of tile entities to register
    val TILE_ENTITY_LIST = arrayOf(
        VOID_CHEST,
        DARK_FOREST,
        GHASTLY_ENARIA_SPAWNER,
        ENARIA_SPAWNER,
        DESERT_OASIS,
        SPELL_ALTAR,
        ENARIAS_ALTAR
    )
}