package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest
import net.minecraft.block.Block
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.BlockItemUseContext
import net.minecraft.state.StateContainer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.ToolType

/**
 * Class representing a void chest that will be a tile entity as well
 *
 * @constructor initializes the block's properties
 */
class BlockVoidChest : AOTDBlockTileEntity(
    "void_chest",
    Properties.create(Material.ROCK)
        .hardnessAndResistance(4.0f, 50.0f)
) {
    init {
        this.defaultState = stateContainer.baseState.with(FACING_PROPERTY, EnumFacing.NORTH)
    }

    override fun getHarvestLevel(state: IBlockState): Int {
        return 2
    }

    override fun getHarvestTool(state: IBlockState): ToolType {
        return ToolType.PICKAXE
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    @OnlyIn(Dist.CLIENT)
    override fun hasCustomBreakingProgress(state: IBlockState): Boolean {
        return true
    }

    override fun getRenderType(state: IBlockState): EnumBlockRenderType {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED
    }

    override fun getShape(state: IBlockState, world: IBlockReader, blockPos: BlockPos): VoxelShape {
        return VOID_CHEST_SHAPE
    }

    override fun getStateForPlacement(context: BlockItemUseContext): IBlockState? {
        // Face the block depending on the placer's horizontal facing
        return this.defaultState.with(FACING_PROPERTY, context.placementHorizontalFacing.opposite)
    }

    override fun onBlockActivated(
        state: IBlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {
        // Test if the tile entity at this position is a void chest (it should be!)
        val tileEntity = worldIn.getTileEntity(pos)
        if (tileEntity is TileEntityVoidChest) {
            // Ensure the player can interact with the chest
            if (playerIn.getResearch().isResearched(ModResearches.VOID_CHEST)) {
                // Let the player interact with the chest
                tileEntity.interact(playerIn)
            } else if (!worldIn.isRemote) {
                playerIn.sendMessage(TextComponentTranslation(LocalizationConstants.VoidChest.DONT_UNDERSTAND))
            }
        }
        return true
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, IBlockState>) {
        builder.add(FACING_PROPERTY)
    }

    override fun createTileEntity(state: IBlockState, world: IBlockReader): TileEntity {
        return TileEntityVoidChest()
    }

    companion object {
        // The facing property of the void chest which tells it which way to open/close
        private val FACING_PROPERTY = BlockHorizontal.HORIZONTAL_FACING

        // The hitbox of the chest is smaller than usual
        private val VOID_CHEST_SHAPE = Block.makeCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
    }
}