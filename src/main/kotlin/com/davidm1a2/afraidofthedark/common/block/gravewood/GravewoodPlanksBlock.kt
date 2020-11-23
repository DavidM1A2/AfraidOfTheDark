package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Class representing gravewood planks
 *
 * @constructor for gravewood planks sets the planks properties
 */
class GravewoodPlanksBlock : AOTDBlock(
    "gravewood_planks",
    Properties.create(Material.WOOD)
        .hardnessAndResistance(2.0f)
        .sound(SoundType.WOOD)
)