package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.pathfinding.PathType
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.DamageSource
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.PlantType
import java.util.*

/**
 * Class representing the imbued cactus block
 *
 * @constructor sets the block's properties
 */
class ImbuedCactusBlock : AOTDBlock(
    "imbued_cactus",
    Properties.create(Material.CACTUS)
        .tickRandomly()
        .sound(SoundType.CLOTH)
        .hardnessAndResistance(0.4f)
), IPlantable {
    init {
        defaultState = this.stateContainer.baseState.with(AGE, 0)
    }

    override fun tick(state: BlockState, world: World, pos: BlockPos, rand: Random) {
        if (!world.isAreaLoaded(pos, 1)) {
            return // Forge: prevent growing cactus from loading unloaded chunks with block update
        }

        if (!state.isValidPosition(world, pos)) {
            world.destroyBlock(pos, true)
        } else {
            // Check if the air block above is air, if so grow
            if (world.isAirBlock(pos.up())) {
                var age = state.get<Int>(AGE)
                age++

                // If we're at max age grow and reset age to 0
                if (age == MAX_AGE) {
                    age = 0

                    // Compute the number of cactus blocks stacked
                    var currentHeight = 0
                    for (yOffset in 0 until MAX_HEIGHT) {
                        val blockBelow = world.getBlockState(pos.down(yOffset))
                        if (blockBelow.block == this) {
                            currentHeight++
                        } else if (blockBelow.block != this) {
                            break
                        }
                    }

                    // If we're at max height, grow a blossom, otherwise grow a cactus block
                    if (currentHeight == MAX_HEIGHT) {
                        world.setBlockState(pos.up(), ModBlocks.IMBUED_CACTUS_BLOSSOM.defaultState)
                    } else {
                        world.setBlockState(pos.up(), this.defaultState)
                    }
                }

                // Update age
                world.setBlockState(pos, state.with(AGE, age))
            }
        }
    }

    override fun getCollisionShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return CACTUS_COLLISION_SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return CACTUS_SHAPE
    }

    override fun isSolid(state: BlockState): Boolean {
        return true
    }

    override fun updatePostPlacement(
        state: BlockState,
        facing: Direction,
        facingState: BlockState,
        world: IWorld,
        currentPos: BlockPos,
        facingPos: BlockPos
    ): BlockState? {
        if (!state.isValidPosition(world, currentPos)) {
            world.pendingBlockTicks.scheduleTick(currentPos, this, 1)
        }
        @Suppress("DEPRECATION")
        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos)
    }

    override fun isValidPosition(state: BlockState, world: IWorldReader, pos: BlockPos): Boolean {
        val blockStateBelow = world.getBlockState(pos.down())
        return blockStateBelow.block.canSustainPlant(
            blockStateBelow,
            world,
            pos.down(),
            Direction.UP,
            this
        ) && !world.getBlockState(pos.up()).material.isLiquid
    }

    override fun canSustainPlant(
        iBlockState: BlockState,
        world: IBlockReader,
        blockPos: BlockPos,
        facing: Direction,
        iPlantable: IPlantable
    ): Boolean {
        val plantStateToPlace = iPlantable.getPlant(world, blockPos.offset(facing))
        return plantStateToPlace.block == this || plantStateToPlace.block == ModBlocks.IMBUED_CACTUS_BLOSSOM
    }

    override fun onEntityCollision(state: BlockState, world: World, blockPos: BlockPos, entity: Entity) {
        if (entity !is ItemEntity || entity.item.item != ModItems.DESERT_FRUIT) {
            entity.attackEntityFrom(DamageSource.CACTUS, 2.0f)
        }
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(AGE)
    }

    override fun allowsMovement(state: BlockState, world: IBlockReader, blockPos: BlockPos, pathType: PathType): Boolean {
        return false
    }

    override fun getPlantType(world: IBlockReader?, pos: BlockPos?): PlantType {
        return PlantType.Desert
    }

    override fun getPlant(world: IBlockReader?, pos: BlockPos?): BlockState {
        return defaultState
    }

    companion object {
        private const val MAX_HEIGHT = 3
        private const val MAX_AGE = 5
        private val AGE = IntegerProperty.create("age", 0, MAX_AGE)
        private val CACTUS_COLLISION_SHAPE = makeCuboidShape(1.0, 0.0, 1.0, 15.0, 15.0, 15.0)
        private val CACTUS_SHAPE = makeCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    }
}