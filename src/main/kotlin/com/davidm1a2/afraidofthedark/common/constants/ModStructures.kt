package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructure
import com.davidm1a2.afraidofthedark.common.world.structure.darkforest.DarkForestStructure
import com.davidm1a2.afraidofthedark.common.world.structure.desertoasis.DesertOasisStructure
import com.davidm1a2.afraidofthedark.common.world.structure.gnomishcity.GnomishCityStairwellClipperStructurePiece
import com.davidm1a2.afraidofthedark.common.world.structure.gnomishcity.GnomishCityStructure
import com.davidm1a2.afraidofthedark.common.world.structure.nightmareisland.NightmareIslandStructure
import com.davidm1a2.afraidofthedark.common.world.structure.observatory.ObservatoryStructure
import com.davidm1a2.afraidofthedark.common.world.structure.voidchest.VoidChestStructure
import com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox.VoidChestBoxStructure
import com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox.VoidChestBoxStructurePiece
import com.davidm1a2.afraidofthedark.common.world.structure.voidchestportal.VoidChestPortalStructure
import com.davidm1a2.afraidofthedark.common.world.structure.witchhut.WitchHutStructure
import net.minecraft.util.ResourceLocation
import net.minecraft.world.gen.feature.structure.IStructurePieceType

/**
 * A list of structures to be registered
 */
object ModStructures {
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

    val STRUCTURES = arrayOf(
        CRYPT
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