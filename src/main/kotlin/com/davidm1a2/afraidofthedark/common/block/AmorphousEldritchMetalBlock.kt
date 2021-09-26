package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class representing amorphous eldritch metal, this can be walked through
 *
 * @constructor initializes the block's properties
 */
class AmorphousEldritchMetalBlock : AOTDBlock(
    "amorphous_eldritch_metal",
    Properties.of(Material.PORTAL)
        .noCollission()
        .lightLevel { 1 }
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
        .strength(10.0f, 50.0f)
        .requiresCorrectToolForDrops()
)
