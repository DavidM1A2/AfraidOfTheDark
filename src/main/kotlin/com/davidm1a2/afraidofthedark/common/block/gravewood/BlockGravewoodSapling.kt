package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockSapling
import com.davidm1a2.afraidofthedark.common.world.gen.tree.GravewoodTree
import net.minecraft.block.material.Material

/**
 * Block representing a gravewood sapling
 *
 * @constructor initializes the sapling with a name
 */
class BlockGravewoodSapling : AOTDBlockSapling("gravewood_sapling", GravewoodTree(), Properties.create(Material.LEAVES))