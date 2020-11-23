package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlabBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Class representing a mangrove slab
 *
 * @constructor sets the name and material
 */
class MangroveSlabBlock : AOTDSlabBlock(
    "mangrove_slab",
    Properties.create(Material.WOOD)
        .hardnessAndResistance(2.0f, 3.0f)
        .sound(SoundType.WOOD)
)