package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

/**
 * Mangrove stair block
 *
 * @constructor sets the name and texture to the mangrove plank texture
 */
class MangroveStairsBlock : AOTDStairsBlock("mangrove_stairs", { ModBlocks.MANGROVE_PLANKS.defaultBlockState() }, Properties.copy(ModBlocks.MANGROVE_PLANKS)) {
    override fun getFireSpreadSpeed(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 20
    }
}