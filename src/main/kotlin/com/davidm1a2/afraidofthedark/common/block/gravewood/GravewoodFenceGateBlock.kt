package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDFenceGateBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

/**
 * Class representing a gravewood fence gate
 *
 * @constructor sets the name and material
 */
class GravewoodFenceGateBlock : AOTDFenceGateBlock("gravewood_fence_gate", Properties.of(Material.WOOD, ModBlocks.GRAVEWOOD.defaultMaterialColor())) {
    override fun getFireSpreadSpeed(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 20
    }
}
