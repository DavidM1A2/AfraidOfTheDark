package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks

/**
 * Mangrove stair block
 *
 * @constructor sets the name and texture to the sacred mangrove plank texture
 */
class SacredMangroveStairsBlock :
    AOTDStairsBlock("sacred_mangrove_stairs", { ModBlocks.SACRED_MANGROVE_PLANKS.defaultState }, Properties.from(ModBlocks.SACRED_MANGROVE_PLANKS))