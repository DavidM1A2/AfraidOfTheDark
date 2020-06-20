package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraftforge.common.ToolType

/**
 * Class representing the igneous block created from 9 igneous gems
 *
 * @constructor sets the block's properties
 */
class BlockIgneous : AOTDBlock(
    "igneous_block",
    Properties.create(Material.ROCK)
        .hardnessAndResistance(4.0f, 1.0f)
        .lightValue(1)
) {
    override fun getHarvestLevel(state: IBlockState): Int {
        return 2
    }

    override fun getHarvestTool(state: IBlockState): ToolType {
        return ToolType.PICKAXE
    }
}