package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.world.level.material.Material

/**
 * Class that represents an astral silver ore block
 *
 * @constructor initializes the block's properties
 */
class AstralCopperOreBlock : AOTDBlock(
    "astral_copper_ore",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .requiresCorrectToolForDrops()
)