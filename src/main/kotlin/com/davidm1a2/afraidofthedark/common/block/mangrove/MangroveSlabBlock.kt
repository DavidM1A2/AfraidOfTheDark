package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlabBlock
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

/**
 * Class representing a mangrove slab
 *
 * @constructor sets the name and material
 */
class MangroveSlabBlock : AOTDSlabBlock(
    "mangrove_slab",
    Properties.of(Material.WOOD)
        .strength(2.0f, 3.0f)
        .sound(SoundType.WOOD)
) {
    override fun getFireSpreadSpeed(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 20
    }
}