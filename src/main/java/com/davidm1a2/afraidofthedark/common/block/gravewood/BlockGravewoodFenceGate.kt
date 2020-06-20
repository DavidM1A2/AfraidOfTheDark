package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockFenceGate
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a gravewood fence gate
 *
 * @constructor sets the name and material
 */
class BlockGravewoodFenceGate : AOTDBlockFenceGate("gravewood_fence_gate", Properties.create(Material.WOOD, ModBlocks.GRAVEWOOD.getRawMaterialColor()))