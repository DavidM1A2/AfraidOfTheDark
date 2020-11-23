package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.tileEntity.EnariaSpawnerTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.GhastlyEnariaSpawnerTileEntity
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import net.minecraft.world.Region
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.common.ToolType
import org.apache.logging.log4j.LogManager

/**
 * Class representing the block that spawns enaria in the nightmare realm
 *
 * @constructor makes the block hard to break and sets the block's name
 */
class EnariaSpawnerBlock : AOTDTileEntityBlock("enaria_spawner", Properties.create(Material.ROCK).hardnessAndResistance(10.0f, 50.0f)) {
    override fun displayInCreative(): Boolean {
        return false
    }

    override fun getHarvestLevel(state: BlockState): Int {
        return 3
    }

    override fun getHarvestTool(state: BlockState): ToolType {
        return ToolType.PICKAXE
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity? {
        val dimType = when (world) {
            is World -> world.dimension.type
            is Region -> world.dimension.type
            else -> throw IllegalStateException("Can't determine the world type for IBlockReader ${world::class.java.simpleName}")
        }
        // In the overworld we spawn a regular enaria, in the nightmare we spawn a ghastly enaria
        return when (dimType) {
            DimensionType.OVERWORLD -> EnariaSpawnerTileEntity()
            ModDimensions.NIGHTMARE_TYPE -> GhastlyEnariaSpawnerTileEntity()
            else -> {
                LOGGER.warn("BlockEnariaSpawner should not exist in this dimension, defaulting to a NO-OP TileEntity.")
                null
            }
        }
    }

    override fun getDrops(p_220076_1_: BlockState, p_220076_2_: LootContext.Builder): List<ItemStack> {
        return emptyList()
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}