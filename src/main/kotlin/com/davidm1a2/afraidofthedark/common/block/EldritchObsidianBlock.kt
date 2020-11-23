package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class that represents an eldritch obsidian block
 *
 * @constructor initializes the block
 */
class EldritchObsidianBlock : AOTDBlock("eldritch_obsidian", Properties.create(Material.ROCK).hardnessAndResistance(10.0f, 50.0f)) {
    override fun getHarvestLevel(state: BlockState): Int {
        return 3
    }

    override fun getHarvestTool(state: BlockState): ToolType {
        return ToolType.PICKAXE
    }
}