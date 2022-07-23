package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.client.gui.screens.SpellListScreen
import com.davidm1a2.afraidofthedark.client.tileEntity.spellCraftingTable.SpellCraftingTableItemStackRenderer
import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.block.core.AOTDUseBlockItemStackRenderer
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.SpellCraftingTableTileEntity
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItemUseContext
import net.minecraft.particles.ParticleTypes
import net.minecraft.state.DirectionProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import java.util.*

class SpellCraftingTableBlock : AOTDTileEntityBlock("spell_crafting_table", Properties.copy(ModBlocks.GRAVEWOOD_PLANKS).lightLevel { 5 }), AOTDUseBlockItemStackRenderer {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun use(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: PlayerEntity,
        hand: Hand,
        hit: BlockRayTraceResult
    ): ActionResultType {
        // If you right-click the table with a spell scroll, pass along the use call to the item, so we can learn the spell
        val itemInHand = playerIn.getItemInHand(hand)
        if (itemInHand.item == ModItems.SPELL_SCROLL && ModItems.SPELL_SCROLL.getSpell(itemInHand) != null) {
            return ActionResultType.PASS
        }

        val research = playerIn.getResearch()
        val hasResearch = research.isResearched(ModResearches.SPELLMASON)
        if (worldIn.isClientSide && hasResearch) {
            Minecraft.getInstance().setScreen(SpellListScreen())
        }
        if (!worldIn.isClientSide && !hasResearch) {
            playerIn.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
        }
        return ActionResultType.SUCCESS
    }

    override fun getISTER(): ItemStackTileEntityRenderer {
        return SpellCraftingTableItemStackRenderer()
    }

    override fun newBlockEntity(world: IBlockReader): TileEntity {
        return SpellCraftingTableTileEntity()
    }

    override fun getCollisionShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun rotate(blockState: BlockState, rotation: Rotation): BlockState {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)))
    }

    override fun mirror(blockState: BlockState, mirror: Mirror): BlockState {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)))
    }

    override fun getStateForPlacement(blockItemUseContext: BlockItemUseContext): BlockState {
        return defaultBlockState().setValue(FACING, blockItemUseContext.horizontalDirection.opposite)
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun animateTick(blockState: BlockState, world: World, blockPos: BlockPos, random: Random) {
        val facing = blockState.getValue(FACING)
        val x = blockPos.x + 0.5 + facing.stepZ * 0.3 - facing.stepX * 0.36
        val y = blockPos.y + 1.2
        val z = blockPos.z + 0.5 - facing.stepX * 0.3 - facing.stepZ * 0.36
        world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0)
        world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0)
    }

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
        private val SHAPE = VoxelShapes.or(
            box(0.0, 12.0, 0.0, 16.0, 14.0, 16.0),
            box(2.0, 0.0, 2.0, 4.0, 14.0, 4.0),
            box(12.0, 0.0, 2.0, 14.0, 14.0, 4.0),
            box(2.0, 0.0, 12.0, 4.0, 14.0, 14.0),
            box(12.0, 0.0, 12.0, 14.0, 14.0, 14.0),
        )
    }
}