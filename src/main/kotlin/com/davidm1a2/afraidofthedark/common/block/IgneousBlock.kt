package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class representing the igneous block created from 9 igneous gems
 *
 * @constructor sets the block's properties
 */
class IgneousBlock : AOTDBlock(
    "igneous_block",
    Properties.of(Material.STONE)
        .strength(4.0f, 1.0f)
        .lightLevel { 1 }
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
)