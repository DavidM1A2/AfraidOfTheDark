package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material

/**
 * Class representing the igneous block created from 9 igneous gems
 *
 * @constructor sets the block's properties
 */
class BlockIgneous : AOTDBlock("igneous_block", Material.ROCK) {
    init {
        blockHardness = 4.0f
        blockResistance = 1.0f
        setLightLevel(1.0f)
        this.setHarvestLevel("pickaxe", 2)
    }
}