package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader

/**
 * Base class for all AOTD blocks that also contain tile entities
 *
 * @constructor initializes the block's material, name, and tile entity flag
 * @param name     The name of the block
 * @param properties The block properties
 */
abstract class AOTDBlockTileEntity(name: String, properties: Properties) : BlockContainer(properties), AOTDShowBlockCreative {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }

    abstract override fun createTileEntity(state: IBlockState, world: IBlockReader): TileEntity?

    /**
     * Deprecated, use createTileEntity() instead
     */
    override fun createNewTileEntity(worldIn: IBlockReader): TileEntity? {
        return null
    }
}