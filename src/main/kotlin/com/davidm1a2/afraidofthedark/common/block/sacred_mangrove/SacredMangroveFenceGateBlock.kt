package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceGateBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

/**
 * Class representing a sacred mangrove fence gate
 *
 * @constructor sets the name and material
 */
class SacredMangroveFenceGateBlock :
    AOTDFenceGateBlock("sacred_mangrove_fence_gate", Properties.of(Material.WOOD, ModBlocks.SACRED_MANGROVE.defaultMaterialColor())) {
    override fun getFireSpreadSpeed(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 20
    }
}