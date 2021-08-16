package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

/**
 * Mangrove stair block
 *
 * @constructor sets the name and texture to the sacred mangrove plank texture
 */
class SacredMangroveStairsBlock :
    AOTDStairsBlock("sacred_mangrove_stairs", { ModBlocks.SACRED_MANGROVE_PLANKS.defaultBlockState() }, Properties.copy(ModBlocks.SACRED_MANGROVE_PLANKS)) {
    override fun getFireSpreadSpeed(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 20
    }
}