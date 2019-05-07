package com.DavidM1A2.afraidofthedark.common.tileEntity.core;

import net.minecraft.block.Block;
import net.minecraft.util.ITickable;

/**
 * Base class for all tile entities that require ticking every game tick
 */
public abstract class AOTDTickingTileEntity extends AOTDTileEntity implements ITickable
{
    // The number of ticks the tile entity has existed
    protected long ticksExisted = 0;

    /**
     * Constructor initializes the tile entity fields
     *
     * @param block The block that this tile entity is for
     */
    public AOTDTickingTileEntity(Block block)
    {
        super(block);
    }

    /**
     * Called every tick to update the tile entity's state
     */
    @Override
    public void update()
    {
        this.ticksExisted = this.ticksExisted + 1;
    }
}
