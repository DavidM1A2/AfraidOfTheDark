package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState

/**
 * Mangrove stair block
 *
 * @constructor sets the name and texture to the sacred mangrove plank texture
 */
class SacredMangroveStairsBlock :
    AOTDStairsBlock("sacred_mangrove_stairs", { ModBlocks.SACRED_MANGROVE_PLANKS.defaultBlockState() }, Properties.copy(ModBlocks.SACRED_MANGROVE_PLANKS)) {
    override fun getFireSpreadSpeed(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 20
    }
}