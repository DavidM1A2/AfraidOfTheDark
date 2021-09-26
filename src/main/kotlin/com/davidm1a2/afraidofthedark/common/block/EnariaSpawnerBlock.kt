package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.tileEntity.EnariaSpawnerTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.GhastlyEnariaSpawnerTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalBlock
import net.minecraft.block.material.Material
import net.minecraft.item.BlockItemUseContext
import net.minecraft.state.StateContainer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraftforge.common.ToolType
import org.apache.logging.log4j.LogManager

/**
 * Class representing the block that spawns enaria in the nightmare realm
 *
 * @constructor makes the block hard to break and sets the block's name
 */
class EnariaSpawnerBlock : AOTDTileEntityBlock(
    "enaria_spawner",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .harvestLevel(3)
        .harvestTool(ToolType.PICKAXE)
) {
    init {
        registerDefaultState(this.stateDefinition.any().setValue(HorizontalBlock.FACING, Direction.NORTH))
    }

    override fun displayInCreative(): Boolean {
        return false
    }

    override fun getRenderShape(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun rotate(state: BlockState, world: IWorld, pos: BlockPos, rotation: Rotation): BlockState {
        return state.setValue(HorizontalBlock.FACING, rotation.rotate(state.getValue(HorizontalBlock.FACING)))
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(HorizontalBlock.FACING, rotation.rotate(state.getValue(HorizontalBlock.FACING)))
    }

    override fun mirror(state: BlockState, mirrorIn: Mirror): BlockState {
        return state.setValue(HorizontalBlock.FACING, mirrorIn.mirror(state.getValue(HorizontalBlock.FACING)))
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState {
        return this.defaultBlockState().setValue(HorizontalBlock.FACING, context.horizontalDirection)
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(HorizontalBlock.FACING)
    }

    override fun newBlockEntity(world: IBlockReader): TileEntity? {
        val dimType = when (world) {
            is World -> world.dimension()
            else -> throw IllegalStateException("Can't determine the world type for IBlockReader ${world::class.java.simpleName}")
        }
        // In the overworld we spawn a regular enaria, in the nightmare we spawn a ghastly enaria
        return when (dimType) {
            World.OVERWORLD -> EnariaSpawnerTileEntity()
            ModDimensions.NIGHTMARE_WORLD -> GhastlyEnariaSpawnerTileEntity()
            else -> {
                LOGGER.warn("BlockEnariaSpawner should not exist in this dimension, defaulting to a NO-OP TileEntity.")
                null
            }
        }
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}