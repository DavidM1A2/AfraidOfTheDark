package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.FeyLightTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader

class FeyLightBlock : AOTDTileEntityBlock(
    "fey_light",
    Properties.of(Material.DECORATION)
        .noCollission()
        .instabreak()
        .noDrops()
        .lightLevel { 15 }) {
    override fun newBlockEntity(world: IBlockReader): TileEntity {
        return FeyLightTileEntity()
    }

    override fun getShape(state: BlockState, world: IBlockReader, blockPos: BlockPos, context: ISelectionContext): VoxelShape {
        return FEY_LIGHT_SHAPE
    }

    companion object {
        private val FEY_LIGHT_SHAPE = Block.box(3.0, 3.0, 3.0, 13.0, 13.0, 13.0)
    }
}