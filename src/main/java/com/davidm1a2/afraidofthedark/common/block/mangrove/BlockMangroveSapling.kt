package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockSapling
import com.davidm1a2.afraidofthedark.common.world.gen.tree.MangroveTree
import net.minecraft.block.material.Material

/**
 * Block representing a mangrove sapling
 *
 * @constructor initializes the sapling with a name
 */
class BlockMangroveSapling : AOTDBlockSapling("mangrove_sapling", MangroveTree(), Properties.create(Material.LEAVES))