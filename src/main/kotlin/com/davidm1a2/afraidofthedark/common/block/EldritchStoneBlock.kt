package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class that represents an eldritch stone block
 *
 * @constructor initializes block properties
 */
class EldritchStoneBlock : AOTDBlock("eldritch_stone", Properties.create(Material.ROCK).hardnessAndResistance(5.0f)) {
    override fun getHarvestLevel(state: BlockState): Int {
        return 1
    }

    override fun getHarvestTool(state: BlockState): ToolType {
        return ToolType.PICKAXE
    }
}