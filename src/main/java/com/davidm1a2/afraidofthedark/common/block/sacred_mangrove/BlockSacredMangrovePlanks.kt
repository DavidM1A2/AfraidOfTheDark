package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Class representing sacred mangrove planks
 *
 * @constructor for mangrove planks sets the planks properties
 */
class BlockSacredMangrovePlanks : AOTDBlock("sacred_mangrove_planks", Material.WOOD) {
    init {
        setHardness(2.0f)
        this.soundType = SoundType.WOOD
    }
}