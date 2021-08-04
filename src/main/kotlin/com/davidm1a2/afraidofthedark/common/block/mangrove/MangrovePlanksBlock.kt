package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Class representing mangrove planks
 *
 * @constructor for mangrove planks sets the planks properties
 */
class MangrovePlanksBlock : AOTDBlock(
    "mangrove_planks",
    Properties.of(Material.WOOD)
        .strength(2.0f)
        .sound(SoundType.WOOD)
)