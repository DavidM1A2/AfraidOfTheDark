package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.block.core.IShowBlockCreative
import com.davidm1a2.afraidofthedark.common.tileEntity.DroppedJournalTileEntity
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.pathfinding.PathType
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import java.util.*

class DroppedJournalBlock : AOTDTileEntityBlock(
    "dropped_journal", Properties.of(Material.PLANT)
        .noCollission()
        .randomTicks()
        .instabreak()
), IShowBlockCreative {
    override fun displayInCreative(): Boolean {
        return false
    }

    override fun use(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        result: BlockRayTraceResult
    ): ActionResultType {
        if (!world.isClientSide) {
            val droppedJournal = world.getBlockEntity(pos)
            if (droppedJournal is DroppedJournalTileEntity) {
                player.addItem(droppedJournal.journalItem)
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
            }
        }
        return ActionResultType.SUCCESS
    }

    override fun getPickBlock(state: BlockState, target: RayTraceResult, world: IBlockReader, pos: BlockPos, player: PlayerEntity): ItemStack {
        val droppedJournal = world.getBlockEntity(pos)
        if (droppedJournal is DroppedJournalTileEntity) {
            return droppedJournal.journalItem
        }
        return ItemStack.EMPTY
    }

    override fun tick(blockState: BlockState, world: ServerWorld, blockPos: BlockPos, random: Random) {
        if (!world.isAreaLoaded(blockPos, 1)) {
            return
        }

        if (!blockState.canSurvive(world, blockPos)) {
            world.destroyBlock(blockPos, true)
        }
    }

    override fun updateShape(blockState: BlockState, direction: Direction, sideState: BlockState, world: IWorld, blockPos: BlockPos, sidePos: BlockPos): BlockState? {
        if (!blockState.canSurvive(world, blockPos)) {
            world.blockTicks.scheduleTick(blockPos, this, 1)
        }
        return super.updateShape(blockState, direction, sideState, world, blockPos, sidePos)
    }

    override fun canSurvive(blockState: BlockState, worldReader: IWorldReader, blockPos: BlockPos): Boolean {
        val belowPos = blockPos.below()
        val belowState = worldReader.getBlockState(belowPos)
        return belowState.isCollisionShapeFullBlock(worldReader, belowPos) && belowState.material.blocksMotion()
    }

    override fun isPathfindable(blockState: BlockState, blockReader: IBlockReader, blockPos: BlockPos, pathType: PathType): Boolean {
        return if (pathType == PathType.AIR) true else super.isPathfindable(blockState, blockReader, blockPos, pathType)
    }

    override fun propagatesSkylightDown(blockState: BlockState, iBlockReader: IBlockReader, blockPos: BlockPos): Boolean {
        return true
    }

    override fun newBlockEntity(blockReader: IBlockReader): TileEntity {
        return DroppedJournalTileEntity()
    }

    override fun getShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return DROPPED_JOURNAL_SHAPE
    }

    companion object {
        private val DROPPED_JOURNAL_SHAPE = box(4.0, 0.0, 4.0, 12.0, 1.5, 12.0)
    }
}