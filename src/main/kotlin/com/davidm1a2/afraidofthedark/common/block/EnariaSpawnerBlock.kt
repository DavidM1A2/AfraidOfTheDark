package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.EnariaSpawnerTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.Material

/**
 * Class representing the block that spawns enaria in the nightmare realm
 *
 * @constructor makes the block hard to break and sets the block's name
 */
class EnariaSpawnerBlock : AOTDTileEntityBlock(
    "enaria_spawner",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
) {
    init {
        registerDefaultState(this.stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH))
    }

    override fun displayInCreative(): Boolean {
        return false
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun rotate(state: BlockState, world: LevelAccessor, pos: BlockPos, rotation: Rotation): BlockState {
        return state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING)))
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING)))
    }

    override fun mirror(state: BlockState, mirrorIn: Mirror): BlockState {
        return state.setValue(HorizontalDirectionalBlock.FACING, mirrorIn.mirror(state.getValue(HorizontalDirectionalBlock.FACING)))
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.horizontalDirection)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HorizontalDirectionalBlock.FACING)
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        // TODO: Find a way to split Enaria & GhastlyEnariaSpawner TileEntities. Perhaps need to consolidate TEs, or make separate blocks
        return EnariaSpawnerTileEntity()
        // return GhastlyEnariaSpawnerTileEntity()?
    }
}