package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.MagicCrystalTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.block.material.PushReaction
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.common.ToolType
import org.apache.logging.log4j.LogManager

class MagicCrystalBlock : AOTDTileEntityBlock(
    "magic_crystal",
    Properties.of(Material.STONE)
        .strength(20.0f, 100.0f)
        .harvestLevel(3)
        .harvestTool(ToolType.PICKAXE)
) {
    init {
        registerDefaultState(this.stateDefinition.any().setValue(BOTTOM, false))
    }

    fun isBottom(blockState: BlockState): Boolean {
        return blockState.getOptionalValue(BOTTOM).orElse(false)
    }

    override fun setPlacedBy(world: World, blockPos: BlockPos, blockState: BlockState, livingEntity: LivingEntity?, itemStack: ItemStack) {
        if (!world.isClientSide) {
            for (i in 1 until BLOCK_HEIGHT) {
                if (world.getBlockState(blockPos.above(i)).material.isReplaceable) {
                    world.setBlockAndUpdate(blockPos.above(i), this.defaultBlockState())
                }
            }
        }
        super.setPlacedBy(world, blockPos, blockState, livingEntity, itemStack)
    }

    override fun removedByPlayer(blockState: BlockState, world: World, blockPos: BlockPos, playerEntity: PlayerEntity, willHarvest: Boolean, ffluidStateuid: FluidState): Boolean {
        clearCrystal(world, blockPos)
        return super.removedByPlayer(blockState, world, blockPos, playerEntity, willHarvest, ffluidStateuid)
    }

    override fun onBlockExploded(blockState: BlockState, world: World, blockPos: BlockPos, explosion: Explosion) {
        clearCrystal(world, blockPos)
        super.onBlockExploded(blockState, world, blockPos, explosion)
    }

    private fun clearCrystal(world: World, blockPos: BlockPos, clearBlockPos: Boolean = false) {
        if (!world.isClientSide) {
            var bottomBlockPos = blockPos
            // While the current block is not the bottom, and it is a magic crystal
            while (world.getBlockState(bottomBlockPos).let { !isBottom(it) && it.block == this }) {
                bottomBlockPos = bottomBlockPos.below()
            }

            val bottomState = world.getBlockState(bottomBlockPos)
            if (bottomState.block != this) {
                LOG.error("Invalid magic crystal detected")
                return
            }

            var ascendingBlockPos = bottomBlockPos
            for (i in 0 until BLOCK_HEIGHT) {
                if (ascendingBlockPos == blockPos && !clearBlockPos) {
                    // Don't do anything special for the current block, just break the ones above and below
                    ascendingBlockPos = ascendingBlockPos.above()
                    continue
                }

                val blockAbove = world.getBlockState(ascendingBlockPos)
                if (blockAbove.block == this) {
                    world.setBlockAndUpdate(ascendingBlockPos, Blocks.AIR.defaultBlockState())
                }
                ascendingBlockPos = ascendingBlockPos.above()
            }
        }
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState? {
        // Don't place this too close to the top of the world
        if (World.isOutsideBuildHeight(context.clickedPos.above(BLOCK_HEIGHT - 1))) {
            return null
        }

        val numBlockingEntities = context.level.getEntitiesOfClass(LivingEntity::class.java, AxisAlignedBB(context.clickedPos).expandTowards(0.0, BLOCK_HEIGHT - 1.0, 0.0)).size
        if (numBlockingEntities > 0) {
            return null
        }

        val world = context.level
        for (i in 0 until BLOCK_HEIGHT) {
            val blockPos = context.clickedPos.above(i)
            if (!world.getBlockState(blockPos).canBeReplaced(context)) {
                return null
            }
        }
        return defaultBlockState().setValue(BOTTOM, true)
    }

    override fun getPistonPushReaction(blockState: BlockState): PushReaction {
        return PushReaction.BLOCK
    }

    override fun getRenderShape(blockState: BlockState): BlockRenderType {
        return BlockRenderType.ENTITYBLOCK_ANIMATED
    }

    override fun newBlockEntity(world: IBlockReader): TileEntity {
        return MagicCrystalTileEntity()
    }

    override fun getCollisionShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(BOTTOM)
    }

    companion object {
        private val LOG = LogManager.getLogger()
        private val SHAPE = box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
        private val BOTTOM = BlockStateProperties.BOTTOM
        const val BLOCK_HEIGHT = 5
    }
}