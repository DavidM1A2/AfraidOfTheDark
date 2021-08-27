package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class representing sunstone ore found in meteors
 *
 * @constructor sets the block's properties like name
 */
class SunstoneOreBlock : AOTDBlock(
    "sunstone_ore",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .lightLevel { 1 }
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
        .requiresCorrectToolForDrops()
)