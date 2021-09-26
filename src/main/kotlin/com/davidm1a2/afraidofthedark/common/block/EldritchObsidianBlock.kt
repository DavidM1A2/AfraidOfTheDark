package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class that represents an eldritch obsidian block
 *
 * @constructor initializes the block
 */
class EldritchObsidianBlock : AOTDBlock(
    "eldritch_obsidian",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .harvestLevel(3)
        .harvestTool(ToolType.PICKAXE)
        .requiresCorrectToolForDrops()
)