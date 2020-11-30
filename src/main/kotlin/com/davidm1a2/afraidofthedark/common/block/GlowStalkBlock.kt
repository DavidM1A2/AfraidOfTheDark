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
    Properties.create(Material.EARTH)
        .lightValue(1)
        .hardnessAndResistance(1.0f, 4.0f)
)