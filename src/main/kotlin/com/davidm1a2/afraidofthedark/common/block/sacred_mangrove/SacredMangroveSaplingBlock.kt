package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSaplingBlock
import net.minecraft.world.level.material.Material

/**
 * Block representing a sacred mangrove sapling
 *
 * @constructor initializes the sapling with a name
 */
class SacredMangroveSaplingBlock : AOTDSaplingBlock(
    "sacred_mangrove_sapling",
    SacredMangroveTree(), Properties.of(Material.LEAVES)
)