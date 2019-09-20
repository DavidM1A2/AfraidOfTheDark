package com.davidm1a2.afraidofthedark.common.item.core;

import com.davidm1a2.afraidofthedark.common.constants.Constants;
import net.minecraft.item.ItemSword;

/**
 * Base class for all AOTD swords
 */
public class AOTDSword extends ItemSword
{
    /**
     * Constructor takes a tool material and name of the item in the constructor
     *
     * @param toolMaterial The tool material to be used for the sword
     * @param baseName     The name of the sword
     */
    public AOTDSword(ToolMaterial toolMaterial, String baseName)
    {
        super(toolMaterial);
        // Set the unlocalized and registry name
        this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
        this.setRegistryName(Constants.MOD_ID + ":" + baseName);
        // If this should be displayed in creative then set the tab
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
