package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSaplingBlock
import net.minecraft.block.material.Material

/**
 * Block representing a mangrove sapling
 *
 * @constructor initializes the sapling with a name
 */
class MangroveSaplingBlock : AOTDSaplingBlock("mangrove_sapling", MangroveTree(), Properties.of(Material.LEAVES))