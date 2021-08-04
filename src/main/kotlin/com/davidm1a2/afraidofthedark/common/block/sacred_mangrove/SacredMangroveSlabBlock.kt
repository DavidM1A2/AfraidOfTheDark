package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlabBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Class representing a sacred mangrove half slab
 *
 * @constructor sets the name and material
 */
class SacredMangroveSlabBlock : AOTDSlabBlock(
    "sacred_mangrove_slab",
    Properties.of(Material.WOOD)
        .strength(2.0f, 3.0f)
        .sound(SoundType.WOOD)
)