package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

/**
 * Class representing gravewood planks
 *
 * @constructor for gravewood planks sets the planks properties
 */
class GravewoodPlanksBlock : AOTDBlock(
    "gravewood_planks",
    Properties.of(Material.WOOD)
        .strength(2.0f)
        .sound(SoundType.WOOD)
) {
    override fun getFireSpreadSpeed(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 20
    }
}
