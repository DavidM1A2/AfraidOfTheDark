package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.FeyLightTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class FeyLightBlock : AOTDTileEntityBlock(
    "fey_light",
    Properties.of(Material.DECORATION)
        .noCollission()
        .instabreak()
        .noDrops()
        .lightLevel { 15 }) {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return FeyLightTileEntity()
    }

    override fun getShape(state: BlockState, world: BlockGetter, blockPos: BlockPos, context: CollisionContext): VoxelShape {
        return FEY_LIGHT_SHAPE
    }

    companion object {
        private val FEY_LIGHT_SHAPE = Block.box(3.0, 3.0, 3.0, 13.0, 13.0, 13.0)
    }
}