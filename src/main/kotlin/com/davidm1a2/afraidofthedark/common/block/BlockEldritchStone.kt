package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraftforge.common.ToolType

/**
 * Class that represents an eldritch stone block
 *
 * @constructor initializes block properties
 */
class BlockEldritchStone : AOTDBlock("eldritch_stone", Properties.create(Material.ROCK).hardnessAndResistance(5.0f)) {
    override fun getHarvestLevel(state: IBlockState): Int {
        return 1
    }

    override fun getHarvestTool(state: IBlockState): ToolType {
        return ToolType.PICKAXE
    }
}