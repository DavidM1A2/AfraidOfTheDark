package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.Item

/**
 * Base class for all AOTD items
 *
 * @constructor sets up item properties
 * @param baseName The name of the item to be used by the game registry
 */
abstract class AOTDItem(baseName: String, displayInCreative: Boolean = true) : Item()
{
    init
    {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")

        if (displayInCreative)
        {
            this.creativeTab = Constants.AOTD_CREATIVE_TAB
        }
    }
}