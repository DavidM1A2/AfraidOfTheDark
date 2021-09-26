package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class representing the gnomish metal strut
 *
 * @constructor sets the name and block properties
 */
class GnomishMetalStrutBlock : AOTDBlock(
    "gnomish_metal_strut",
    Properties.of(Material.STONE)
        .strength(2.0f, 10.0f)
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
        .requiresCorrectToolForDrops()
)