package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.client.gui.screens.SpellListScreen
import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.block.core.IUseBlockItemStackRenderer
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.SpellCraftingTableTileEntity
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.*

class SpellCraftingTableBlock : AOTDTileEntityBlock("spell_crafting_table", Properties.copy(ModBlocks.GRAVEWOOD_PLANKS).lightLevel { 5 }), IUseBlockItemStackRenderer {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun use(
        state: BlockState,
        worldIn: Level,
        pos: BlockPos,
        playerIn: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        // If you right-click the table with a spell scroll, pass along the use call to the item, so we can learn the spell
        val itemInHand = playerIn.getItemInHand(hand)
        if (itemInHand.item == ModItems.SPELL_SCROLL && ModItems.SPELL_SCROLL.getSpell(itemInHand) != null) {
            return InteractionResult.PASS
        }

        val research = playerIn.getResearch()
        val hasResearch = research.isResearched(ModResearches.SPELLMASON)
        if (worldIn.isClientSide && hasResearch) {
            Minecraft.getInstance().setScreen(SpellListScreen())
        }
        if (!worldIn.isClientSide && !hasResearch) {
            playerIn.sendMessage(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
        }
        return InteractionResult.SUCCESS
    }

    override fun getItemRenderer(): ItemRenderer {
        // TODO: Register SpellCraftingTableItemStackRenderer for item via public void initializeClient(@Nonnull Consumer<IItemRenderProperties> consumer) {
        // override fun getISTER(): ItemStackTileEntityRenderer {
        //     return SpellCraftingTableItemStackRenderer()
        // }
        TODO("Not yet implemented")
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return SpellCraftingTableTileEntity()
    }

    override fun getCollisionShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun rotate(blockState: BlockState, rotation: Rotation): BlockState {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)))
    }

    override fun mirror(blockState: BlockState, mirror: Mirror): BlockState {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)))
    }

    override fun getStateForPlacement(blockItemUseContext: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, blockItemUseContext.horizontalDirection.opposite)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun animateTick(blockState: BlockState, world: Level, blockPos: BlockPos, random: Random) {
        val facing = blockState.getValue(FACING)
        val x = blockPos.x + 0.5 + facing.stepZ * 0.3 - facing.stepX * 0.36
        val y = blockPos.y + 1.2
        val z = blockPos.z + 0.5 - facing.stepX * 0.3 - facing.stepZ * 0.36
        world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0)
        world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0)
    }

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
        private val SHAPE = Shapes.or(
            box(0.0, 12.0, 0.0, 16.0, 14.0, 16.0),
            box(2.0, 0.0, 2.0, 4.0, 14.0, 4.0),
            box(12.0, 0.0, 2.0, 14.0, 14.0, 4.0),
            box(2.0, 0.0, 12.0, 4.0, 14.0, 14.0),
            box(12.0, 0.0, 12.0, 14.0, 14.0, 14.0),
        )
    }
}