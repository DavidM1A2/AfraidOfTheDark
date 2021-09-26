package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material

/**
 * Class representing the glow stalk mushroom stem
 *
 * @constructor sets the item name and makes it glow
 */
class GlowStalkBlock : AOTDBlock(
    "glow_stalk",
    Properties.of(Material.STONE)
        .lightLevel { 1 }
        .strength(1.0f, 4.0f)
)