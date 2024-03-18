package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.Material

/**
 * Class that represents a meteor block
 *
 * @constructor initializes the block's properties
 */
class MeteorBlock : AOTDBlock(
    "meteor", Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .sound(SoundType.METAL)
        .requiresCorrectToolForDrops()
)