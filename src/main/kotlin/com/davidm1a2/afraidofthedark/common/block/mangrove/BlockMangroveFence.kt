package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockFence
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a mangrove fence
 *
 * @constructor sets the name and material
 */
class BlockMangroveFence : AOTDBlockFence("mangrove_fence", Properties.create(Material.WOOD, ModBlocks.MANGROVE.getRawMaterialColor()))