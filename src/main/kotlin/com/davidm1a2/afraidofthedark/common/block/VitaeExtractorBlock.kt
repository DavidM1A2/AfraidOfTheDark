package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor.VitaeExtractorItemStackRenderer
import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.block.core.AOTDUseBlockItemStackRenderer
import com.davidm1a2.afraidofthedark.common.tileEntity.VitaeExtractorTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalBlock
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import net.minecraft.item.BlockItemUseContext
import net.minecraft.state.DirectionProperty
import net.minecraft.state.StateContainer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.world.IBlockReader

class VitaeExtractorBlock : AOTDTileEntityBlock(
    "vitae_extractor",
    Properties.of(Material.STONE)
), AOTDUseBlockItemStackRenderer {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun getISTER(): ItemStackTileEntityRenderer {
        return VitaeExtractorItemStackRenderer()
    }

    override fun newBlockEntity(blockReader: IBlockReader): TileEntity {
        return VitaeExtractorTileEntity()
    }

    override fun rotate(blockState: BlockState, rotation: Rotation): BlockState {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)))
    }

    override fun mirror(blockState: BlockState, mirror: Mirror): BlockState? {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)))
    }

    override fun getStateForPlacement(blockItemUseContext: BlockItemUseContext): BlockState {
        return defaultBlockState().setValue(FACING, blockItemUseContext.horizontalDirection.opposite)
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    companion object {
        val FACING: DirectionProperty = HorizontalBlock.FACING
    }
}