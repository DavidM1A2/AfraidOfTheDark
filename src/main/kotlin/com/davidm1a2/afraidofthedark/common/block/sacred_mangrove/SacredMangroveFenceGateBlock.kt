package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceGateBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

/**
 * Class representing a sacred mangrove fence gate
 *
 * @constructor sets the name and material
 */
class SacredMangroveFenceGateBlock :
    AOTDFenceGateBlock("sacred_mangrove_fence_gate", Properties.of(Material.WOOD, ModBlocks.SACRED_MANGROVE.defaultMaterialColor())) {
    override fun getFireSpreadSpeed(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 20
    }
}