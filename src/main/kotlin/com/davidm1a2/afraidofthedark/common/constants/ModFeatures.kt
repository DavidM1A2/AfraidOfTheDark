package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.feature.structure.base.SchematicStructurePiece
import com.davidm1a2.afraidofthedark.common.feature.structure.crypt.CryptStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.darkforest.DarkForestStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.desertoasis.DesertOasisStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.gnomishcity.GnomishCityStairwellClipperStructurePiece
import com.davidm1a2.afraidofthedark.common.feature.structure.gnomishcity.GnomishCityStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.nightmareisland.NightmareIslandStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.observatory.ObservatoryStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.voidchest.VoidChestStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.voidchestbox.VoidChestBoxStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.voidchestbox.VoidChestBoxStructurePiece
import com.davidm1a2.afraidofthedark.common.feature.structure.voidchestportal.VoidChestPortalStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.witchhut.WitchHutStructure
import com.davidm1a2.afraidofthedark.common.feature.tree.MangroveTreeFeature
import com.davidm1a2.afraidofthedark.common.feature.tree.SacredMangroveTreeFeature
import net.minecraft.util.ResourceLocation
import net.minecraft.world.gen.feature.structure.IStructurePieceType

/**
 * A list of features to be registered
 */
object ModFeatures {
    val CRYPT = CryptStructure()
    val WITCH_HUT = WitchHutStructure()
    val VOID_CHEST = VoidChestStructure()
    val OBSERVATORY = ObservatoryStructure()
    val DARK_FOREST = DarkForestStructure()
    val VOID_CHEST_BOX = VoidChestBoxStructure()
    val VOID_CHEST_PORTAL = VoidChestPortalStructure()
    val NIGHTMARE_ISLAND = NightmareIslandStructure()
    val GNOMISH_CITY = GnomishCityStructure()
    val DESERT_OASIS = DesertOasisStructure()

    val MANGROVE_TREE = MangroveTreeFeature()
    val SACRED_MANGROVE_TREE = SacredMangroveTreeFeature()

    val FEATURES = arrayOf(
        CRYPT,
        WITCH_HUT,
        VOID_CHEST,
        OBSERVATORY,
        DARK_FOREST,
        VOID_CHEST_BOX,
        VOID_CHEST_PORTAL,
        NIGHTMARE_ISLAND,
        GNOMISH_CITY,
        DESERT_OASIS,
        MANGROVE_TREE,
        SACRED_MANGROVE_TREE
    )

    val SCHEMATIC_STRUCTURE_PIECE = IStructurePieceType { _, nbt -> SchematicStructurePiece(nbt) }
    val VOID_BOX_STRUCTURE_PIECE = IStructurePieceType { _, nbt -> VoidChestBoxStructurePiece(nbt) }
    val GNOMISH_CITY_STAIRWELL_CLIPPER_STRUCTURE_PIECE = IStructurePieceType { _, nbt -> GnomishCityStairwellClipperStructurePiece(nbt) }

    val STRUCTURE_PIECES = listOf(
        ResourceLocation(Constants.MOD_ID, "schematic_structure_piece") to SCHEMATIC_STRUCTURE_PIECE,
        ResourceLocation(Constants.MOD_ID, "void_chest_box_structure_piece") to VOID_BOX_STRUCTURE_PIECE,
        ResourceLocation(Constants.MOD_ID, "gnomish_city_stairwell_clipper_structure_piece") to GNOMISH_CITY_STAIRWELL_CLIPPER_STRUCTURE_PIECE
    )
}