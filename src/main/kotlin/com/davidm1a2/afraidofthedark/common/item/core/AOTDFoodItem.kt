package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.Food
import net.minecraft.item.Item
import net.minecraft.world.item.Item

/**
 * Base class for all AOTD food items
 *
 * @constructor sets up item properties
 * @param baseName The name of the item to be used by the game registry
 * @param displayInCreative True if the item should show up in creative, false otherwise
 */
abstract class AOTDFoodItem(
    baseName: String,
    foodAmount: Int,
    saturation: Float,
    properties: Properties,
    isWolfFood: Boolean = false,
    displayInCreative: Boolean = true
) : Item(properties.apply {
    if (displayInCreative) {
        tab(Constants.AOTD_CREATIVE_TAB)
    }
    food(Food.Builder()
        .nutrition(foodAmount)
        .saturationMod(saturation)
        .apply { if (isWolfFood) meat() }
        .build())
}) {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}