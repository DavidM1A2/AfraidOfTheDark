package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks

/**
 * Mangrove stair block
 *
 * @constructor sets the name and texture to the mangrove plank texture
 */
class MangroveStairsBlock : AOTDStairsBlock("mangrove_stairs", { ModBlocks.MANGROVE_PLANKS.defaultBlockState() }, Properties.copy(ModBlocks.MANGROVE_PLANKS))
