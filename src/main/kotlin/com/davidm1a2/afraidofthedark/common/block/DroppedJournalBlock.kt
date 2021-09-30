package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDShowBlockCreative
import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.tileEntity.DroppedJournalTileEntity
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.World

class DroppedJournalBlock : AOTDTileEntityBlock(
    "dropped_journal", Properties.of(Material.PLANT)
        .noCollission()
        .instabreak()
), AOTDShowBlockCreative {
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
            player.addItem(ItemStack(ModItems.JOURNAL))
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
        }
        return ActionResultType.SUCCESS
    }

    override fun getPickBlock(state: BlockState, target: RayTraceResult, world: IBlockReader, pos: BlockPos, player: PlayerEntity): ItemStack {
        return ItemStack(ModItems.JOURNAL)
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