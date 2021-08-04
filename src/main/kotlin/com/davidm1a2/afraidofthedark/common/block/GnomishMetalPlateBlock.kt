package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class representing the gnomish metal plate
 *
 * @constructor sets the name and block properties
 */
class GnomishMetalPlateBlock : AOTDBlock(
    "gnomish_metal_plate",
    Properties.of(Material.STONE)
        .strength(2.0f, 10.0f)
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
)