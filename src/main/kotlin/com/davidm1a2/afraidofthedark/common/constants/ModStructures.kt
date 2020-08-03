package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructure
import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.darkforest.DarkForestStructure
import com.davidm1a2.afraidofthedark.common.world.structure.darkforest.DarkForestStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.observatory.ObservatoryStructure
import com.davidm1a2.afraidofthedark.common.world.structure.observatory.ObservatoryStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.voidchest.VoidChestStructure
import com.davidm1a2.afraidofthedark.common.world.structure.voidchest.VoidChestStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox.VoidChestBoxStructure
import com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox.VoidChestBoxStructurePiece
import com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox.VoidChestBoxStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.voidchestportal.VoidChestPortalStructure
import com.davidm1a2.afraidofthedark.common.world.structure.voidchestportal.VoidChestPortalStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.witchhut.WitchHutStructure
import com.davidm1a2.afraidofthedark.common.world.structure.witchhut.WitchHutStructureStart
import net.minecraft.util.ResourceLocation

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

    val STRUCTURES = listOf(
        CRYPT,
        WITCH_HUT,
        VOID_CHEST,
        OBSERVATORY,
        DARK_FOREST,
        VOID_CHEST_BOX,
        VOID_CHEST_PORTAL
    )

    val STRUCTURE_STARTS = listOf(
        CRYPT to CryptStructureStart::class.java,
        WITCH_HUT to WitchHutStructureStart::class.java,
        VOID_CHEST to VoidChestStructureStart::class.java,
        OBSERVATORY to ObservatoryStructureStart::class.java,
        DARK_FOREST to DarkForestStructureStart::class.java,
        VOID_CHEST_BOX to VoidChestBoxStructureStart::class.java,
        VOID_CHEST_PORTAL to VoidChestPortalStructureStart::class.java
    )

    val STRUCTURE_PIECES = listOf(
        ResourceLocation(Constants.MOD_ID, "schematic_structure_piece") to SchematicStructurePiece::class.java,
        ResourceLocation(Constants.MOD_ID, "void_chest_box_structure_piece") to VoidChestBoxStructurePiece::class.java
    )
}