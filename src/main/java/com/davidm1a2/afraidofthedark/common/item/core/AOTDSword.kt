package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.ItemSword

/**
 * Base class for all AOTD swords
 *
 * @constructor takes a tool material and name of the item in the constructor
 * @param baseName     The name of the sword
 * @param toolMaterial The tool material to be used for the sword
 * @param displayInCreative True if this item should be displayed in creative mode, false otherwise
 */
open class AOTDSword(baseName: String, toolMaterial: ToolMaterial, displayInCreative: Boolean = false) :
    ItemSword(toolMaterial) {
    init {
        // Set the unlocalized and registry name
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")

        // If this should be displayed in creative then set the tab
        if (displayInCreative) {
            this.creativeTab = Constants.AOTD_CREATIVE_TAB
        }
    }
}