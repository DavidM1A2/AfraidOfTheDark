package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.Item

/**
 * Base class for all AOTD items
 *
 * @constructor sets up item properties
 * @param baseName The name of the item to be used by the game registry
 * @param displayInCreative True if the item should show up in creative, false otherwise
 */
abstract class AOTDItem(
    baseName: String,
    properties: Properties,
    displayInCreative: Boolean = true
) : Item(properties.apply {
    if (displayInCreative) {
        tab(Constants.AOTD_CREATIVE_TAB)
    }
}) {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}