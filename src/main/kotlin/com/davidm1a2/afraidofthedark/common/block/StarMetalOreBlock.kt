package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.world.level.material.Material

/**
 * Class representing star metal ore found in meteors
 *
 * @constructor initializes the block's name and properties
 */
class StarMetalOreBlock : AOTDBlock(
    "star_metal_ore",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .lightLevel { 4 }
        .requiresCorrectToolForDrops()
)