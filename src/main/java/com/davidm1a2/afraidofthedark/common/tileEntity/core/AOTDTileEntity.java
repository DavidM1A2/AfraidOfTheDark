package com.davidm1a2.afraidofthedark.common.tileEntity.core;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 * Base class for all AOTD tile entities
 */
public abstract class AOTDTileEntity extends TileEntity
{
    /**
     * Constructor initializes the tile entity fields
     *
     * @param block The block that this tile entity is for
     */
    public AOTDTileEntity(Block block)
    {
        super();
        this.blockType = block;
    }
}
