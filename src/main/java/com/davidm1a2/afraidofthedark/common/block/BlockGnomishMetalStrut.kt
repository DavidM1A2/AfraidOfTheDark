package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraftforge.common.ToolType

/**
 * Class representing the gnomish metal strut
 *
 * @constructor sets the name and block properties
 */
class BlockGnomishMetalStrut : AOTDBlock("gnomish_metal_strut", Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f)) {
    override fun getHarvestLevel(state: IBlockState): Int {
        return 2
    }

    override fun getHarvestTool(state: IBlockState): ToolType {
        return ToolType.PICKAXE
    }
}