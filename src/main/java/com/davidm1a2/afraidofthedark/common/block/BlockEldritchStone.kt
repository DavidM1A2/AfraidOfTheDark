package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material

/**
 * Class that represents an eldritch stone block
 *
 * @constructor initializes block properties
 */
class BlockEldritchStone : AOTDBlock("eldritch_stone", Material.ROCK) {
    init {
        setHardness(5.0f)
        this.setHarvestLevel("pickaxe", 1)
    }
}