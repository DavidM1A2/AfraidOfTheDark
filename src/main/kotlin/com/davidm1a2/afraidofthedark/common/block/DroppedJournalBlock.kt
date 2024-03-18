package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.block.core.IShowBlockCreative
import com.davidm1a2.afraidofthedark.common.tileEntity.DroppedJournalTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
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
        world: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        result: BlockHitResult
    ): InteractionResult {
        if (!world.isClientSide) {
            val droppedJournal = world.getBlockEntity(pos)
            if (droppedJournal is DroppedJournalTileEntity) {
                player.addItem(droppedJournal.journalItem)
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
            }
        }
        return InteractionResult.SUCCESS
    }

    override fun getPickBlock(state: BlockState, target: HitResult, world: BlockGetter, pos: BlockPos, player: Player): ItemStack {
        val droppedJournal = world.getBlockEntity(pos)
        if (droppedJournal is DroppedJournalTileEntity) {
            return droppedJournal.journalItem
        }
        return ItemStack.EMPTY
    }

    override fun tick(blockState: BlockState, world: ServerLevel, blockPos: BlockPos, random: Random) {
        if (!world.isAreaLoaded(blockPos, 1)) {
            return
        }

        if (!blockState.canSurvive(world, blockPos)) {
            world.destroyBlock(blockPos, true)
        }
    }

    override fun updateShape(blockState: BlockState, direction: Direction, sideState: BlockState, world: LevelAccessor, blockPos: BlockPos, sidePos: BlockPos): BlockState? {
        if (!blockState.canSurvive(world, blockPos)) {
            world.blockTicks.scheduleTick(blockPos, this, 1)
        }
        return super.updateShape(blockState, direction, sideState, world, blockPos, sidePos)
    }

    override fun canSurvive(blockState: BlockState, worldReader: LevelReader, blockPos: BlockPos): Boolean {
        val belowPos = blockPos.below()
        val belowState = worldReader.getBlockState(belowPos)
        return belowState.isCollisionShapeFullBlock(worldReader, belowPos) && belowState.material.blocksMotion()
    }

    override fun isPathfindable(blockState: BlockState, blockReader: BlockGetter, blockPos: BlockPos, pathType: PathComputationType): Boolean {
        return if (pathType == PathComputationType.AIR) true else super.isPathfindable(blockState, blockReader, blockPos, pathType)
    }

    override fun propagatesSkylightDown(blockState: BlockState, iBlockReader: BlockGetter, blockPos: BlockPos): Boolean {
        return true
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return DroppedJournalTileEntity()
    }

    override fun getShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return DROPPED_JOURNAL_SHAPE
    }

    companion object {
        private val DROPPED_JOURNAL_SHAPE = box(4.0, 0.0, 4.0, 12.0, 1.5, 12.0)
    }
}