package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor.VitaeExtractorItemStackRenderer
import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.block.core.IUseBlockItemStackRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.item.VitaeLanternItem
import com.davidm1a2.afraidofthedark.common.tileEntity.VitaeExtractorTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalBlock
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemStack
import net.minecraft.state.DirectionProperty
import net.minecraft.state.StateContainer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.common.ToolType
import java.util.*

class VitaeExtractorBlock : AOTDTileEntityBlock(
    "vitae_extractor",
    Properties.of(Material.STONE)
        .harvestTool(ToolType.PICKAXE)
        .requiresCorrectToolForDrops()
        .strength(1.5F, 6.0F)
), IUseBlockItemStackRenderer {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun use(blockState: BlockState, world: World, blockPos: BlockPos, playerEntity: PlayerEntity, hand: Hand, rayTraceResult: BlockRayTraceResult): ActionResultType {
        val itemInHand = playerEntity.getItemInHand(hand)
        val tileEntity = (world.getBlockEntity(blockPos) as? VitaeExtractorTileEntity) ?: return ActionResultType.FAIL
        val lantern = tileEntity.getLantern()

        return if (itemInHand.isEmpty) {
            if (lantern.isEmpty) {
                ActionResultType.CONSUME
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
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f
                    )
                }
                ActionResultType.SUCCESS
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
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f
                    )
                }
                ActionResultType.SUCCESS
            } else {
                ActionResultType.CONSUME
            }
        } else if (tileEntity.isValidFuel(itemInHand)) {
            if (!world.isClientSide && !tileEntity.isBurningFuel()) {
                tileEntity.insertFuel(itemInHand)
                if (!playerEntity.isCreative) {
                    playerEntity.setItemInHand(hand, itemInHand.apply { shrink(1) })
                }
            }
            ActionResultType.SUCCESS
        } else {
            ActionResultType.CONSUME
        }
    }

    override fun animateTick(blockState: BlockState, world: World, blockPos: BlockPos, random: Random) {
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

    private fun summonParticle(blockState: BlockState, world: World, blockPos: BlockPos, random: Random) {
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

    override fun getCollisionShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun getISTER(): ItemStackTileEntityRenderer {
        return VitaeExtractorItemStackRenderer()
    }

    override fun newBlockEntity(blockReader: IBlockReader): TileEntity {
        return VitaeExtractorTileEntity()
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

    companion object {
        private val SHAPE = box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0)
        val FACING: DirectionProperty = HorizontalBlock.FACING
    }
}