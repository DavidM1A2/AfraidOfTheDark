package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

/**
 * Gravewood stair block
 *
 * @constructor sets the name and texture to the gravewood plank texture
 */
class GravewoodStairsBlock :
    AOTDStairsBlock("gravewood_stairs", { ModBlocks.GRAVEWOOD_PLANKS.defaultBlockState() }, Properties.copy(ModBlocks.GRAVEWOOD_PLANKS)) {
    override fun getFireSpreadSpeed(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 20
    }
}