package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockSlab
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Class representing a gravewood slab
 *
 * @constructor sets the name and material
 */
class BlockGravewoodSlab : AOTDBlockSlab(
    "gravewood_slab",
    Properties.create(Material.WOOD)
        .hardnessAndResistance(2.0f, 3.0f)
        .sound(SoundType.WOOD)
)