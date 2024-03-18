package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

/**
 * Class representing a void chest that will be a tile entity as well
 *
 * @constructor initializes the block's properties
 */
class VoidChestBlock : AOTDTileEntityBlock(
    "void_chest",
    Properties.of(Material.STONE)
        .strength(4.0f, 50.0f)
        .requiresCorrectToolForDrops()
) {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING_PROPERTY, Direction.NORTH))
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.ENTITYBLOCK_ANIMATED
    }

    override fun getShape(state: BlockState, world: BlockGetter, blockPos: BlockPos, context: CollisionContext): VoxelShape {
        return VOID_CHEST_SHAPE
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        // Face the block depending on the placer's horizontal facing
        return this.defaultBlockState().setValue(FACING_PROPERTY, context.horizontalDirection.opposite)
    }

    override fun use(
        state: BlockState,
        worldIn: Level,
        pos: BlockPos,
        playerIn: Player,
        hand: InteractionHand,
        result: BlockHitResult
    ): InteractionResult {
        // Test if the tile entity at this position is a void chest (it should be!)
        val tileEntity = worldIn.getBlockEntity(pos)
        if (tileEntity is VoidChestTileEntity) {
            // Ensure the player can interact with the chest
            if (playerIn.getResearch().isResearched(ModResearches.VOID_CHEST)) {
                // Let the player interact with the chest
                tileEntity.interact(playerIn)
            } else if (!worldIn.isClientSide) {
                playerIn.sendMessage(TranslatableComponent("message.afraidofthedark.void_chest.dont_understand"))
            }
        }
        return InteractionResult.SUCCESS
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING_PROPERTY)
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return VoidChestTileEntity()
    }

    companion object {
        // The facing property of the void chest which tells it which way to open/close
        private val FACING_PROPERTY = HorizontalDirectionalBlock.FACING

        // The hitbox of the chest is smaller than usual
        private val VOID_CHEST_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
    }
}