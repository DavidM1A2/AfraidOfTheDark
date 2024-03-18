package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.tileEntity.MagicCrystalTileEntity
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraftforge.common.MinecraftForge
import java.util.*

class MagicCrystalBlock : AOTDTileEntityBlock(
    "magic_crystal",
    Properties.of(Material.STONE)
        .strength(20.0f, 100.0f)
) {
    init {
        registerDefaultState(this.stateDefinition.any().setValue(BOTTOM, false))
    }

    fun isBottom(blockState: BlockState): Boolean {
        return blockState.getOptionalValue(BOTTOM).orElse(false)
    }

    override fun setPlacedBy(world: Level, blockPos: BlockPos, blockState: BlockState, livingEntity: LivingEntity?, itemStack: ItemStack) {
        if (!world.isClientSide) {
            for (i in 1 until BLOCK_HEIGHT) {
                if (world.getBlockState(blockPos.above(i)).material.isReplaceable) {
                    world.setBlockAndUpdate(blockPos.above(i), this.defaultBlockState())
                }
            }
            val masterCrystal = world.getBlockEntity(blockPos) as MagicCrystalTileEntity
            masterCrystal.setMaxVitae()
        }
        super.setPlacedBy(world, blockPos, blockState, livingEntity, itemStack)
    }

    override fun updateShape(
        state: BlockState,
        facing: Direction,
        facingState: BlockState,
        world: LevelAccessor,
        currentPos: BlockPos,
        facingPos: BlockPos
    ): BlockState {
        if (!isValid(world, currentPos)) {
            world.blockTicks.scheduleTick(currentPos, this, 1)
        }

        @Suppress("DEPRECATION")
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, rand: Random) {
        if (!isValid(world, pos)) {
            world.destroyBlock(pos, true)
        }
    }

    private fun isValid(world: LevelAccessor, pos: BlockPos): Boolean {
        if (isBottom(world.getBlockState(pos))) {
            for (i in 1 until BLOCK_HEIGHT) {
                if (world.getBlockState(pos.above(i)).block != this) {
                    return false
                }
            }
            return true
        } else {
            for (i in 1 until BLOCK_HEIGHT) {
                val belowPos = pos.below(i)
                val blockState = world.getBlockState(belowPos)
                // No bottom = invalid
                if (blockState.block != this) {
                    return false
                }
                // Delegate to the bottom
                if (isBottom(blockState)) {
                    return isValid(world, belowPos)
                }
            }
            return false
        }
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        // Don't place this too close to the top of the world
        if (context.level.isOutsideBuildHeight(context.clickedPos.above(BLOCK_HEIGHT - 1))) {
            return null
        }

        val numBlockingEntities = context.level.getEntitiesOfClass(LivingEntity::class.java, AABB(context.clickedPos).expandTowards(0.0, BLOCK_HEIGHT - 1.0, 0.0)).size
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

    override fun use(blockState: BlockState, world: Level, blockPos: BlockPos, player: Player, hand: InteractionHand, rayTraceResult: BlockHitResult): InteractionResult {
        val research = player.getResearch()
        if (!research.isResearched(ModResearches.ELEMENTAL_MAGIC)) {
            if (!world.isClientSide) {
                player.sendMessage(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
            return InteractionResult.CONSUME
        }

        if (!world.isClientSide) {
            val magicCrystalTileEntity = world.getBlockEntity(blockPos) as MagicCrystalTileEntity
            val vitaePercent = magicCrystalTileEntity.getVitae() / magicCrystalTileEntity.getMaxVitae()
            val translationKey = when {
                vitaePercent == 0.0 -> "empty"
                vitaePercent < 0.33 -> "one_quarter"
                vitaePercent < 0.66 -> "half"
                vitaePercent < 1.0 -> "three_quarters"
                else -> "full"
            }
            player.sendMessage(TranslatableComponent("message.afraidofthedark.magic_crystal.$translationKey"))
            MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(player, ModResearches.ADVANCED_MAGIC))
        }
        return InteractionResult.SUCCESS
    }

    override fun getPistonPushReaction(blockState: BlockState): PushReaction {
        return PushReaction.BLOCK
    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.ENTITYBLOCK_ANIMATED
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return MagicCrystalTileEntity()
    }

    override fun getCollisionShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BOTTOM)
    }

    companion object {
        private val SHAPE = box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
        val BOTTOM: BooleanProperty = BlockStateProperties.BOTTOM
        const val BLOCK_HEIGHT = 5
    }
}