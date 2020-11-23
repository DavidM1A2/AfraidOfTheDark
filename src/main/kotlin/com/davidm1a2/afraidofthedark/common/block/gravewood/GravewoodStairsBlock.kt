package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks

/**
 * Gravewood stair block
 *
 * @constructor sets the name and texture to the gravewood plank texture
 */
class GravewoodStairsBlock : AOTDStairsBlock("gravewood_stairs", { ModBlocks.GRAVEWOOD_PLANKS.defaultState }, Properties.from(ModBlocks.GRAVEWOOD_PLANKS))
