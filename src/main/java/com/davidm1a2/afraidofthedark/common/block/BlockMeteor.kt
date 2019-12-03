package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material

/**
 * Class that represents a meteor block
 *
 * @constructor initializes the block's properties
 */
class BlockMeteor : AOTDBlock("meteor", Material.ROCK)
{
    init
    {
        setHardness(10.0f)
        setResistance(50.0f)
        this.setHarvestLevel("pickaxe", 2)
    }
}