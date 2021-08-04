package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceGateBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a gravewood fence gate
 *
 * @constructor sets the name and material
 */
class GravewoodFenceGateBlock : AOTDFenceGateBlock("gravewood_fence_gate", Properties.of(Material.WOOD, ModBlocks.GRAVEWOOD.defaultMaterialColor()))