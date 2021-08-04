package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBushBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorldReader
import net.minecraftforge.common.PlantType

class ImbuedCactusBlossomBlock : AOTDBushBlock(
    "imbued_cactus_blossom",
    Properties.of(Material.PLANT, MaterialColor.WOOD)
        .noCollission()
        .strength(0.0f)
        .sound(SoundType.CROP)
) {
    override fun canSurvive(state: BlockState, world: IWorldReader, blockPos: BlockPos): Boolean {
        val blockOn = world.getBlockState(blockPos.below())
        @Suppress("DEPRECATION")
        return super.canSurvive(state, world, blockPos) && blockOn.block == ModBlocks.IMBUED_CACTUS
    }

    override fun mayPlaceOn(state: BlockState, world: IBlockReader, blockPos: BlockPos): Boolean {
        return state.block == ModBlocks.IMBUED_CACTUS
    }

    override fun getShape(state: BlockState, world: IBlockReader, blockPos: BlockPos, context: ISelectionContext): VoxelShape {
        return IMBUED_CACTUS_BLOSSOM_SHAPE
    }

    override fun getPlantType(world: IBlockReader, pos: BlockPos): PlantType {
        return PlantType.DESERT
    }

    companion object {
        private val IMBUED_CACTUS_BLOSSOM_SHAPE = box(3.0, 0.0, 3.0, 12.0, 16.0, 12.0)
    }
}