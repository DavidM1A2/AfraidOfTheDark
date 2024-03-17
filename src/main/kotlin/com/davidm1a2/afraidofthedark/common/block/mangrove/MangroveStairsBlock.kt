package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairsBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState

/**
 * Mangrove stair block
 *
 * @constructor sets the name and texture to the mangrove plank texture
 */
class MangroveStairsBlock : AOTDStairsBlock("mangrove_stairs", { ModBlocks.MANGROVE_SLAB.defaultBlockState() }, Properties.copy(ModBlocks.MANGROVE_PLANKS)) {
    override fun getFireSpreadSpeed(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 20
    }
}