package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState

/**
 * Gravewood stair block
 *
 * @constructor sets the name and texture to the gravewood plank texture
 */
class GravewoodStairsBlock :
    AOTDStairsBlock("gravewood_stairs", { ModBlocks.GRAVEWOOD_PLANKS.defaultBlockState() }, Properties.copy(ModBlocks.GRAVEWOOD_PLANKS)) {
    override fun getFireSpreadSpeed(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 20
    }
}