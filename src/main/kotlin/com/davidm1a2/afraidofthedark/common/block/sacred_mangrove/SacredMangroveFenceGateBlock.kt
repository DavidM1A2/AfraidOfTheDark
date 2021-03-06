package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceGateBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a sacred mangrove fence gate
 *
 * @constructor sets the name and material
 */
class SacredMangroveFenceGateBlock :
    AOTDFenceGateBlock("sacred_mangrove_fence_gate", Properties.create(Material.WOOD, ModBlocks.SACRED_MANGROVE.materialColor))