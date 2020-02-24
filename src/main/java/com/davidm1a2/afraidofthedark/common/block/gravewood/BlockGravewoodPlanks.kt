package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Class representing gravewood planks
 *
 * @constructor for gravewood planks sets the planks properties
 */
class BlockGravewoodPlanks : AOTDBlock("gravewood_planks", Material.WOOD) {
    init {
        setHardness(2.0f)
        this.soundType = SoundType.WOOD
    }
}