package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockState
import net.minecraft.block.ContainerBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader

/**
 * Base class for all AOTD blocks that also contain tile entities
 *
 * @constructor initializes the block's material, name, and tile entity flag
 * @param name     The name of the block
 * @param properties The block properties
 */
abstract class AOTDTileEntityBlock(name: String, properties: Properties) : ContainerBlock(properties), AOTDShowBlockCreative {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity? {
        return newBlockEntity(world)
    }
}