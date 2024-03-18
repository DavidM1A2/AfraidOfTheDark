package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.pathfinder.BlockPathTypes
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
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
    Properties.of(Material.CACTUS)
        .randomTicks()
        .sound(SoundType.WOOL)
        .strength(0.4f)
), IPlantable, BonemealableBlock {
    init {
        registerDefaultState(stateDefinition.any().setValue(AGE, 0))
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, rand: Random) {
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
                        world.setBlock(pos.above(), ModBlocks.IMBUED_CACTUS_BLOSSOM.defaultBlockState(), UPDATE_ALL)
                    } else {
                        world.setBlock(pos.above(), this.defaultBlockState(), UPDATE_ALL)
                    }
                }

                // Update age
                world.setBlock(pos, state.setValue(AGE, age), UPDATE_ALL)
            }
        }
    }

    override fun getCollisionShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return CACTUS_COLLISION_SHAPE
    }

    override fun getShape(blockState: BlockState, worldIn: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return CACTUS_SHAPE
    }

    override fun updateShape(
        state: BlockState,
        facing: Direction,
        facingState: BlockState,
        world: LevelAccessor,
        currentPos: BlockPos,
        facingPos: BlockPos
    ): BlockState {
        if (!state.canSurvive(world, currentPos)) {
            world.blockTicks.scheduleTick(currentPos, this, 1)
        }

        @Suppress("DEPRECATION")
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos)
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
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
        world: BlockGetter,
        blockPos: BlockPos,
        facing: Direction,
        iPlantable: IPlantable
    ): Boolean {
        val plantStateToPlace = iPlantable.getPlant(world, blockPos.relative(facing))
        return plantStateToPlace.block == this || plantStateToPlace.block == ModBlocks.IMBUED_CACTUS_BLOSSOM
    }

    override fun entityInside(state: BlockState, world: Level, blockPos: BlockPos, entity: Entity) {
        if (entity !is ItemEntity || entity.item.item != ModItems.DESERT_FRUIT) {
            entity.hurt(DamageSource.CACTUS, 2.0f)
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(AGE)
    }

    override fun isPathfindable(state: BlockState, world: BlockGetter, blockPos: BlockPos, pathType: PathComputationType): Boolean {
        return false
    }

    override fun getAiPathNodeType(blockState: BlockState, world: BlockGetter, pos: BlockPos, entity: Mob?): BlockPathTypes {
        return BlockPathTypes.DAMAGE_CACTUS
    }

    override fun getPlantType(world: BlockGetter?, pos: BlockPos?): PlantType {
        return PlantType.DESERT
    }

    override fun getPlant(world: BlockGetter?, pos: BlockPos?): BlockState {
        return defaultBlockState()
    }

    override fun isValidBonemealTarget(world: BlockGetter, blockPos: BlockPos, blockState: BlockState, isClientSide: Boolean): Boolean {
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

    override fun isBonemealSuccess(world: Level, random: Random, blockPos: BlockPos, blockState: BlockState): Boolean {
        return random.nextDouble() < 0.4
    }

    override fun performBonemeal(world: ServerLevel, random: Random, blockPos: BlockPos, blockState: BlockState) {
        world.setBlock(blockPos.above(), ModBlocks.IMBUED_CACTUS_BLOSSOM.defaultBlockState(), UPDATE_ALL)
    }

    companion object {
        private const val MAX_HEIGHT = 3
        private const val MAX_AGE = 5
        private val AGE = IntegerProperty.create("age", 0, MAX_AGE)
        private val CACTUS_COLLISION_SHAPE = box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0)
        private val CACTUS_SHAPE = box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    }
}