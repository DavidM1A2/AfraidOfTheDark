package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.IGrowable
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.pathfinding.PathNodeType
import net.minecraft.pathfinding.PathType
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.util.DamageSource
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.PlantType
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.Constants.BlockFlags
import java.util.*

/**
 * Class representing the imbued cactus block
 *
 * @constructor sets the block's properties
 */
class ImbuedCactusBlock : AOTDBlock(
    "imbued_cactus",
    Properties.of(Material.CACTUS)
        .randomTicks()
        .sound(SoundType.WOOL)
        .strength(0.4f)
), IPlantable, IGrowable {
    init {
        registerDefaultState(stateDefinition.any().setValue(AGE, 0))
    }

    override fun tick(state: BlockState, world: ServerWorld, pos: BlockPos, rand: Random) {
        if (!world.isAreaLoaded(pos, 1)) {
            return // Forge: prevent growing cactus from loading unloaded chunks with block update
        }

        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true)
        } else {
            // Check if the air block above is air, if so grow
            if (world.isEmptyBlock(pos.above())) {
                var age = state.getValue(AGE)
                age++

                // If we're at max age grow and reset age to 0
                if (age == MAX_AGE) {
                    age = 0

                    // Compute the number of cactus blocks stacked
                    var currentHeight = 0
                    for (yOffset in 0 until MAX_HEIGHT) {
                        val blockBelow = world.getBlockState(pos.below(yOffset))
                        if (blockBelow.block == this) {
                            currentHeight++
                        } else if (blockBelow.block != this) {
                            break
                        }
                    }

                    // If we're at max height, grow a blossom, otherwise grow a cactus block
                    if (currentHeight == MAX_HEIGHT) {
                        world.setBlock(pos.above(), ModBlocks.IMBUED_CACTUS_BLOSSOM.defaultBlockState(), Constants.BlockFlags.DEFAULT)
                    } else {
                        world.setBlock(pos.above(), this.defaultBlockState(), Constants.BlockFlags.DEFAULT)
                    }
                }

                // Update age
                world.setBlock(pos, state.setValue(AGE, age), BlockFlags.DEFAULT)
            }
        }
    }

    override fun getCollisionShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return CACTUS_COLLISION_SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return CACTUS_SHAPE
    }

    override fun updateShape(
        state: BlockState,
        facing: Direction,
        facingState: BlockState,
        world: IWorld,
        currentPos: BlockPos,
        facingPos: BlockPos
    ): BlockState {
        if (!state.canSurvive(world, currentPos)) {
            world.blockTicks.scheduleTick(currentPos, this, 1)
        }

        @Suppress("DEPRECATION")
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos)
    }

    override fun canSurvive(state: BlockState, world: IWorldReader, pos: BlockPos): Boolean {
        val blockStateBelow = world.getBlockState(pos.below())
        return blockStateBelow.block.canSustainPlant(
            blockStateBelow,
            world,
            pos.below(),
            Direction.UP,
            this
        ) && !world.getBlockState(pos.above()).material.isLiquid
    }

    override fun canSustainPlant(
        iBlockState: BlockState,
        world: IBlockReader,
        blockPos: BlockPos,
        facing: Direction,
        iPlantable: IPlantable
    ): Boolean {
        val plantStateToPlace = iPlantable.getPlant(world, blockPos.relative(facing))
        return plantStateToPlace.block == this || plantStateToPlace.block == ModBlocks.IMBUED_CACTUS_BLOSSOM
    }

    override fun entityInside(state: BlockState, world: World, blockPos: BlockPos, entity: Entity) {
        if (entity !is ItemEntity || entity.item.item != ModItems.DESERT_FRUIT) {
            entity.hurt(DamageSource.CACTUS, 2.0f)
        }
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(AGE)
    }

    override fun isPathfindable(state: BlockState, world: IBlockReader, blockPos: BlockPos, pathType: PathType): Boolean {
        return false
    }

    override fun getAiPathNodeType(blockState: BlockState, world: IBlockReader, pos: BlockPos, entity: MobEntity?): PathNodeType {
        return PathNodeType.DAMAGE_CACTUS
    }

    override fun getPlantType(world: IBlockReader?, pos: BlockPos?): PlantType {
        return PlantType.DESERT
    }

    override fun getPlant(world: IBlockReader?, pos: BlockPos?): BlockState {
        return defaultBlockState()
    }

    override fun isValidBonemealTarget(world: IBlockReader, blockPos: BlockPos, blockState: BlockState, isClientSide: Boolean): Boolean {
        // Compute the number of cactus blocks stacked
        var currentHeight = 0
        for (yOffset in 0 until MAX_HEIGHT) {
            val blockBelow = world.getBlockState(blockPos.below(yOffset))
            if (blockBelow.block == this) {
                currentHeight++
            } else if (blockBelow.block != this) {
                break
            }
        }
        if (currentHeight < MAX_HEIGHT) {
            return false
        }

        return world.getBlockState(blockPos.above()).isAir
    }

    override fun isBonemealSuccess(world: World, random: Random, blockPos: BlockPos, blockState: BlockState): Boolean {
        return random.nextDouble() < 0.4
    }

    override fun performBonemeal(world: ServerWorld, random: Random, blockPos: BlockPos, blockState: BlockState) {
        world.setBlock(blockPos.above(), ModBlocks.IMBUED_CACTUS_BLOSSOM.defaultBlockState(), BlockFlags.DEFAULT)
    }

    companion object {
        private const val MAX_HEIGHT = 3
        private const val MAX_AGE = 5
        private val AGE = IntegerProperty.create("age", 0, MAX_AGE)
        private val CACTUS_COLLISION_SHAPE = box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0)
        private val CACTUS_SHAPE = box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    }
}