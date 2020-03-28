package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.ItemFood

/**
 * Base class for all AOTD food items
 *
 * @constructor sets up item properties
 * @param baseName The name of the item to be used by the game registry
 * @param displayInCreative True if the item should show up in creative, false otherwise
 */
abstract class AOTDItemFood(
    baseName: String,
    foodAmount: Int,
    saturation: Float,
    isWolfFood: Boolean = false,
    displayInCreative: Boolean = true
) : ItemFood(foodAmount, saturation, isWolfFood) {
    init {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")

        if (displayInCreative) {
            this.creativeTab = Constants.AOTD_CREATIVE_TAB
        }
    }
}