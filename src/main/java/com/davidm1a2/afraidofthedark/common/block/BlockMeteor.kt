package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraftforge.common.ToolType

/**
 * Class that represents a meteor block
 *
 * @constructor initializes the block's properties
 */
class BlockMeteor : AOTDBlock(
    "meteor", Properties.create(Material.ROCK)
        .hardnessAndResistance(10.0f, 50.0f)
        .sound(SoundType.METAL)
) {
    /**
     * Gets the harvest level for a block (-1 = none, 0 = wood, 1 = stone, 2 = iron, 3 = diamond)
     *
     * @param state The block's state
     * @return The harvest level
     */
    override fun getHarvestLevel(state: IBlockState): Int {
        return 2
    }

    /**
     * Gets the harvest tool for a block
     *
     * @param state The block's state
     * @return The harvest tool type
     */
    override fun getHarvestTool(state: IBlockState): ToolType {
        return ToolType.PICKAXE
    }
}