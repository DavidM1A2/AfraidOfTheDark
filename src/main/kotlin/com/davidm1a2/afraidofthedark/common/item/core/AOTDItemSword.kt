package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.IItemTier
import net.minecraft.item.ItemSword

/**
 * Base class for all AOTD swords
 *
 * @constructor takes a tool material and name of the item in the constructor
 * @param baseName     The name of the sword
 * @param toolMaterial The tool material to be used for the sword
 * @param displayInCreative True if this item should be displayed in creative mode, false otherwise
 */
open class AOTDItemSword(
    baseName: String,
    toolMaterial: IItemTier,
    damageAmplifier: Int,
    attackSpeed: Float,
    properties: Properties,
    displayInCreative: Boolean = false
) : ItemSword(toolMaterial, damageAmplifier, attackSpeed, properties.apply {
    if (displayInCreative) {
        group(Constants.AOTD_CREATIVE_TAB)
    }
}) {
    init {
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
    }
}