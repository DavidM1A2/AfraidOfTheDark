package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSaplingBlock
import com.davidm1a2.afraidofthedark.common.world.tree.GravewoodTree
import net.minecraft.block.material.Material

/**
 * Block representing a gravewood sapling
 *
 * @constructor initializes the sapling with a name
 */
class GravewoodSaplingBlock : AOTDSaplingBlock(
    "gravewood_sapling",
    GravewoodTree(), Properties.create(Material.LEAVES)
)