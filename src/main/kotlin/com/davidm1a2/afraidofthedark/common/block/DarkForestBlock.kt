package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.DarkForestTileEntity
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import net.minecraftforge.common.ToolType

/**
 * Dark forest block used to monitor the state of the dark forest dungeon
 *
 * @constructor sets the block name and properties
 */
class DarkForestBlock : AOTDTileEntityBlock(
    "dark_forest",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
        .harvestLevel(3)
        .harvestTool(ToolType.PICKAXE)
) {
    override fun displayInCreative(): Boolean {
        return false
    }

    override fun getRenderShape(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun newBlockEntity(world: IBlockReader): TileEntity {
        return DarkForestTileEntity()
    }
}