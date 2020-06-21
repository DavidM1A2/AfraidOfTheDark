package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityEnariaSpawner
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

    override fun createTileEntity(state: IBlockState, world: IBlockReader): TileEntity {
        // In the overworld we spawn a regular enaria, in the nightmare we spawn a ghastly enaria
        return TileEntityEnariaSpawner()
    }

    override fun getItemsToDropCount(state: IBlockState, fortune: Int, world: World, pos: BlockPos, random: Random): Int {
        return 0
    }
}