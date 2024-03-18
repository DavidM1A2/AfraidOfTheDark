package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.world.level.material.Material

/**
 * Class representing the igneous block created from 9 igneous gems
 *
 * @constructor sets the block's properties
 */
class IgneousBlock : AOTDBlock(
    "igneous_block",
    Properties.of(Material.STONE)
        .strength(4.0f, 1.0f)
        .lightLevel { 1 }
        .requiresCorrectToolForDrops()
)