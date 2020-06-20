package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockFence
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a gravewood fence
 *
 * @constructor sets the name and material
 */
class BlockGravewoodFence : AOTDBlockFence("gravewood_fence", Properties.create(Material.WOOD, ModBlocks.GRAVEWOOD.getRawMaterialColor()))