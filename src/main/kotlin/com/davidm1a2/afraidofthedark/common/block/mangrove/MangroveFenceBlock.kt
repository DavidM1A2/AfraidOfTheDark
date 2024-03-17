package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

/**
 * Class representing a mangrove fence
 *
 * @constructor sets the name and material
 */
class MangroveFenceBlock : AOTDFenceBlock("mangrove_fence", Properties.of(Material.WOOD, ModBlocks.MANGROVE.defaultMaterialColor())) {
    override fun getFireSpreadSpeed(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 20
    }
}