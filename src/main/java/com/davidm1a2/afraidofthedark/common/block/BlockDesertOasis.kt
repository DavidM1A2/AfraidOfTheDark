package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityDesertOasis
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.common.ToolType
import java.util.*

/**
 * Desert oasis block used to monitor the state of the desert oasis dungeon
 *
 * @constructor sets the block name and properties
 */
class BlockDesertOasis : AOTDBlockTileEntity(
    "desert_oasis",
    Properties.create(Material.ROCK)
        .hardnessAndResistance(10.0f, 50.0f)
) {
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

    override fun createTileEntity(state: IBlockState, world: IBlockReader): TileEntity {
        return TileEntityDesertOasis()
    }

    override fun getItemsToDropCount(state: IBlockState, fortune: Int, world: World, pos: BlockPos, random: Random): Int {
        return 0
    }
}