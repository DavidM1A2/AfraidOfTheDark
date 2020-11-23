package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.DarkForestTileEntity
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.common.ToolType

/**
 * Dark forest block used to monitor the state of the dark forest dungeon
 *
 * @constructor sets the block name and properties
 */
class DarkForestBlock : AOTDTileEntityBlock(
    "dark_forest",
    Properties.create(Material.ROCK)
        .hardnessAndResistance(10.0f, 50.0f)
) {
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

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity {
        return DarkForestTileEntity()
    }

    override fun getDrops(state: BlockState, lootContext: LootContext.Builder): List<ItemStack> {
        return emptyList()
    }
}