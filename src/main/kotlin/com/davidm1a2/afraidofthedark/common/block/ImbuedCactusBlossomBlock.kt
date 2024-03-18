package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBushBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraftforge.common.PlantType

class ImbuedCactusBlossomBlock : AOTDBushBlock(
    "imbued_cactus_blossom",
    Properties.of(Material.PLANT, MaterialColor.WOOD)
        .noCollission()
        .strength(0.0f)
        .sound(SoundType.CROP)
) {
    override fun canSurvive(state: BlockState, world: LevelReader, blockPos: BlockPos): Boolean {
        val blockOn = world.getBlockState(blockPos.below())
        @Suppress("DEPRECATION")
        return super.canSurvive(state, world, blockPos) && blockOn.block == ModBlocks.IMBUED_CACTUS
    }

    override fun mayPlaceOn(state: BlockState, world: BlockGetter, blockPos: BlockPos): Boolean {
        return state.block == ModBlocks.IMBUED_CACTUS
    }

    override fun getShape(state: BlockState, world: BlockGetter, blockPos: BlockPos, context: CollisionContext): VoxelShape {
        return IMBUED_CACTUS_BLOSSOM_SHAPE
    }

    override fun getPlantType(world: BlockGetter, pos: BlockPos): PlantType {
        return PlantType.DESERT
    }

    companion object {
        private val IMBUED_CACTUS_BLOSSOM_SHAPE = box(3.0, 0.0, 3.0, 12.0, 16.0, 12.0)
    }
}