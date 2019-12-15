package com.davidm1a2.afraidofthedark.common.tileEntity.core

import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity

/**
 * Base class for all AOTD tile entities
 *
 * @constructor initializes the tile entity fields
 * @param block The block that this tile entity is for
 */
abstract class AOTDTileEntity(block: Block) : TileEntity()
{
    init
    {
        blockType = block
    }
}