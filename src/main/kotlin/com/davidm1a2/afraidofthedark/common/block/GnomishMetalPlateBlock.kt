package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class representing the gnomish metal plate
 *
 * @constructor sets the name and block properties
 */
class GnomishMetalPlateBlock : AOTDBlock("gnomish_metal_plate", Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f)) {
    override fun getHarvestLevel(state: BlockState): Int {
        return 2
    }

    override fun getHarvestTool(state: BlockState): ToolType {
        return ToolType.PICKAXE
    }
}