package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalBlock
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItemUseContext
import net.minecraft.state.StateContainer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.common.ToolType

/**
 * Class representing a void chest that will be a tile entity as well
 *
 * @constructor initializes the block's properties
 */
class VoidChestBlock : AOTDTileEntityBlock(
    "void_chest",
    Properties.of(Material.STONE)
        .strength(4.0f, 50.0f)
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
) {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING_PROPERTY, Direction.NORTH))
    }

    override fun getRenderShape(state: BlockState): BlockRenderType {
        return BlockRenderType.ENTITYBLOCK_ANIMATED
    }

    override fun getShape(state: BlockState, world: IBlockReader, blockPos: BlockPos, context: ISelectionContext): VoxelShape {
        return VOID_CHEST_SHAPE
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState? {
        // Face the block depending on the placer's horizontal facing
        return this.defaultBlockState().setValue(FACING_PROPERTY, context.horizontalDirection.opposite)
    }

    override fun use(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: PlayerEntity,
        hand: Hand,
        result: BlockRayTraceResult
    ): ActionResultType {
        // Test if the tile entity at this position is a void chest (it should be!)
        val tileEntity = worldIn.getBlockEntity(pos)
        if (tileEntity is VoidChestTileEntity) {
            // Ensure the player can interact with the chest
            if (playerIn.getResearch().isResearched(ModResearches.VOID_CHEST)) {
                // Let the player interact with the chest
                tileEntity.interact(playerIn)
            } else if (!worldIn.isClientSide) {
                playerIn.sendMessage(TranslationTextComponent("message.afraidofthedark.void_chest.dont_understand"), playerIn.uuid)
            }
        }
        return ActionResultType.SUCCESS
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(FACING_PROPERTY)
    }

    override fun newBlockEntity(world: IBlockReader): TileEntity {
        return VoidChestTileEntity()
    }

    companion object {
        // The facing property of the void chest which tells it which way to open/close
        private val FACING_PROPERTY = HorizontalBlock.FACING

        // The hitbox of the chest is smaller than usual
        private val VOID_CHEST_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
    }
}