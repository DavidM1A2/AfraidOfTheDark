package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityEnariaSpawner
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityGhastlyEnariaSpawner
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.Region
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.ToolType
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Class representing the block that spawns enaria in the nightmare realm
 *
 * @constructor makes the block hard to break and sets the block's name
 */
class BlockEnariaSpawner : AOTDBlockTileEntity("enaria_spawner", Properties.create(Material.ROCK).hardnessAndResistance(10.0f, 50.0f)) {
    override fun displayInCreative(): Boolean {
        return false
    }

    override fun getHarvestLevel(state: IBlockState): Int {
        return 3
    }

    override fun getHarvestTool(state: IBlockState): ToolType {
        return ToolType.PICKAXE
    }

    override fun getRenderType(state: IBlockState): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }

    override fun createTileEntity(state: IBlockState, world: IBlockReader): TileEntity? {
        val dimType = when (world) {
            is World -> world.dimension.type
            is Region -> world.dimension.type
            else -> throw IllegalStateException("Can't determine the world type for IBlockReader ${world::class.java.simpleName}")
        }
        // In the overworld we spawn a regular enaria, in the nightmare we spawn a ghastly enaria
        return when (dimType) {
            DimensionType.OVERWORLD -> TileEntityEnariaSpawner()
            ModDimensions.NIGHTMARE_TYPE -> TileEntityGhastlyEnariaSpawner()
            else -> {
                LOGGER.warn("BlockEnariaSpawner should not exist in this dimension, defaulting to a NO-OP TileEntity.")
                null
            }
        }
    }

    override fun getItemsToDropCount(state: IBlockState, fortune: Int, world: World, pos: BlockPos, random: Random): Int {
        return 0
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}