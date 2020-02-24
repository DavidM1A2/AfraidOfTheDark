package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material

/**
 * Class representing the gnomish metal strut
 *
 * @constructor sets the name and block properties
 */
class BlockGnomishMetalStrut : AOTDBlock("gnomish_metal_strut", Material.ROCK) {
    init {
        setHardness(2.0f)
        setResistance(10.0f)
        this.setHarvestLevel("pickaxe", 2)
    }
}