package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.block.core.IUseBlockItemStackRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.item.VitaeLanternItem
import com.davidm1a2.afraidofthedark.common.tileEntity.VitaeExtractorTileEntity
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.*

class VitaeExtractorBlock : AOTDTileEntityBlock(
    "vitae_extractor",
    Properties.of(Material.STONE)
        .requiresCorrectToolForDrops()
        .strength(1.5F, 6.0F)
), IUseBlockItemStackRenderer {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun use(blockState: BlockState, world: Level, blockPos: BlockPos, playerEntity: Player, hand: InteractionHand, rayTraceResult: BlockHitResult): InteractionResult {
        val itemInHand = playerEntity.getItemInHand(hand)
        val tileEntity = (world.getBlockEntity(blockPos) as? VitaeExtractorTileEntity) ?: return InteractionResult.FAIL
        val lantern = tileEntity.getLantern()

        return if (itemInHand.isEmpty) {
            if (lantern.isEmpty) {
                InteractionResult.CONSUME
            } else {
                if (!world.isClientSide) {
                    tileEntity.clearLantern()
                    playerEntity.setItemInHand(hand, lantern)
                    world.playSound(
                        null,
                        blockPos.x + 0.5,
                        blockPos.y + 0.5,
                        blockPos.z + 0.5,
                        SoundEvents.ITEM_FRAME_REMOVE_ITEM,
                        SoundSource.BLOCKS,
                        1.0f,
                        1.0f
                    )
                }
                InteractionResult.SUCCESS
            }
        } else if (itemInHand.item == ModItems.VITAE_LANTERN) {
            if (lantern.isEmpty) {
                if (!world.isClientSide) {
                    tileEntity.insertLantern(itemInHand)
                    playerEntity.setItemInHand(hand, ItemStack.EMPTY)
                    world.playSound(
                        null,
                        blockPos.x + 0.5,
                        blockPos.y + 0.5,
                        blockPos.z + 0.5,
                        SoundEvents.LANTERN_PLACE,
                        SoundSource.BLOCKS,
                        1.0f,
                        1.0f
                    )
                }
                InteractionResult.SUCCESS
            } else {
                InteractionResult.CONSUME
            }
        } else if (tileEntity.isValidFuel(itemInHand)) {
            if (!world.isClientSide && !tileEntity.isBurningFuel()) {
                tileEntity.insertFuel(itemInHand)
                if (!playerEntity.isCreative) {
                    playerEntity.setItemInHand(hand, itemInHand.apply { shrink(1) })
                }
            }
            InteractionResult.SUCCESS
        } else {
            InteractionResult.CONSUME
        }
    }

    override fun animateTick(blockState: BlockState, world: Level, blockPos: BlockPos, random: Random) {
        val tileEntity = world.getBlockEntity(blockPos)
        if (tileEntity is VitaeExtractorTileEntity && tileEntity.isBurningFuel()) {
            summonParticle(blockState, world, blockPos, random)
            summonParticle(blockState, world, blockPos, random)
            val lantern = tileEntity.getLantern()
            if (!lantern.isEmpty && ModItems.VITAE_LANTERN.getChargeLevel(lantern) != VitaeLanternItem.ChargeLevel.FULL) {
                val centerX = blockPos.x + 0.5
                val centerY = blockPos.y + 0.5
                val centerZ = blockPos.z + 0.5

                world.addParticle(ModParticles.VITAE_EXTRACTOR_CHARGE, centerX, centerY, centerZ, 0.0, 0.0, 0.0)
            }
        }
    }

    private fun summonParticle(blockState: BlockState, world: Level, blockPos: BlockPos, random: Random) {
        val centerX = blockPos.x + 0.5
        val centerY = blockPos.y.toDouble()
        val centerZ = blockPos.z + 0.5

        val facing = blockState.getValue(FACING)
        val facingDirection = facing.axis
        val xOffset = if (facingDirection == Direction.Axis.X) {
            facing.stepX * 0.44
        } else {
            random.nextDouble() * 0.6 - 0.3
        }
        val yOffset = random.nextDouble() * 7.0 / 16.0
        val zOffset = if (facingDirection == Direction.Axis.Z) {
            facing.stepZ * 0.44
        } else {
            random.nextDouble() * 0.6 - 0.3
        }

        world.addParticle(ModParticles.VITAE_EXTRACTOR_BURN, centerX + xOffset, centerY + yOffset, centerZ + zOffset, 0.0, 0.0, 0.0)
    }

    override fun getCollisionShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun getItemRenderer(): ItemRenderer {
        // TODO: Register VitaeExtractorItemStackRenderer for item via public void initializeClient(@Nonnull Consumer<IItemRenderProperties> consumer) {
        // override fun getISTER(): ItemStackTileEntityRenderer {
        //     return VitaeExtractorItemStackRenderer()
        // }
        TODO("Not yet implemented")
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return VitaeExtractorTileEntity()
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

    companion object {
        private val SHAPE = box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0)
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
    }
}