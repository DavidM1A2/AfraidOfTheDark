package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks

class EldritchStoneStairsBlock : AOTDStairsBlock(
    "eldritch_stone_stairs",
    { ModBlocks.ELDRITCH_STONE.defaultBlockState() },
    Properties.copy(ModBlocks.ELDRITCH_STONE)
)