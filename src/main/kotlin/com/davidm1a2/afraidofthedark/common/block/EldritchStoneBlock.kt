package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class that represents an eldritch stone block
 *
 * @constructor initializes block properties
 */
class EldritchStoneBlock : AOTDBlock(
    "eldritch_stone",
    Properties.of(Material.STONE)
        .strength(5.0f)
        .harvestLevel(1)
        .harvestTool(ToolType.PICKAXE)
)