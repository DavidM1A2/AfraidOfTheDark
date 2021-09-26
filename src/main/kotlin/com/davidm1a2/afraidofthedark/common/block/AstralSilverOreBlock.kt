package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class that represents an astral silver ore block
 *
 * @constructor initializes the block's properties
 */
class AstralSilverOreBlock : AOTDBlock(
    "astral_silver_ore",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
        .requiresCorrectToolForDrops()
)
