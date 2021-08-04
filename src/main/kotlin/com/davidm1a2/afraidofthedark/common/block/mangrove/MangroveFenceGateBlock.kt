package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceGateBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a mangrove fence gate
 *
 * @constructor sets the name and material
 */
class MangroveFenceGateBlock : AOTDFenceGateBlock("mangrove_fence_gate", Properties.of(Material.WOOD, ModBlocks.MANGROVE.defaultMaterialColor()))