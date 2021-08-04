package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlabBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Class representing a gravewood slab
 *
 * @constructor sets the name and material
 */
class GravewoodSlabBlock : AOTDSlabBlock(
    "gravewood_slab",
    Properties.of(Material.WOOD)
        .strength(2.0f, 3.0f)
        .sound(SoundType.WOOD)
)