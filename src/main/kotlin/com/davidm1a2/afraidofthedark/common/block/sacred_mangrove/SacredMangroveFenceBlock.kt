package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a sacred mangrove fence
 *
 * @constructor sets the name and material
 */
class SacredMangroveFenceBlock : AOTDFenceBlock("sacred_mangrove_fence", Properties.create(Material.WOOD, ModBlocks.SACRED_MANGROVE.materialColor))