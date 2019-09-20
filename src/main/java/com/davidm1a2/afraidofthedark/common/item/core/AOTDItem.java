package com.davidm1a2.afraidofthedark.common.item.core;

import com.davidm1a2.afraidofthedark.common.constants.Constants;
import net.minecraft.item.Item;

/**
 * Base class for all AOTD items
 */
public abstract class AOTDItem extends Item
{
    /**
     * Constructor sets up item properties
     *
     * @param baseName The name of the item to be used by the game registry
     */
    public AOTDItem(String baseName)
    {
        super();
        this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
        this.setRegistryName(Constants.MOD_ID + ":" + baseName);
        if (this.displayInCreative())
        {
            this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
        }
    }

    /**
     * @return True if this item should be displayed in creative mode, false otherwise
     */
    protected boolean displayInCreative()
    {
        return true;
    }
}
