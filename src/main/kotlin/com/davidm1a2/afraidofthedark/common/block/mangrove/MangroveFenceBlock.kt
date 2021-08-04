package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material

/**
 * Class representing a mangrove fence
 *
 * @constructor sets the name and material
 */
class MangroveFenceBlock : AOTDFenceBlock("mangrove_fence", Properties.of(Material.WOOD, ModBlocks.MANGROVE.defaultMaterialColor()))