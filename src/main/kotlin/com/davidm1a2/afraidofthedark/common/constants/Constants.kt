/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

/**
 * References for static final variables
 */
object Constants {
    // The ID of the mod
    const val MOD_ID = "afraidofthedark"

    // Creative Tab for the mod
    val AOTD_CREATIVE_TAB = object : ItemGroup(MOD_ID) {
        /**
         * Getter for the mod creative tab icon
         *
         * @return The icon for the creative tab as an item
         */
        override fun createIcon(): ItemStack {
            return ItemStack(ModItems.JOURNAL)
        }
    }

    // The base size of all GUIs
    const val BASE_GUI_WIDTH = 640
    const val BASE_GUI_HEIGHT = 360

    // All text will be rendered with this scale factor to avoid blurry text
    const val TEXT_SCALE_FACTOR = 0.25f

    // The distance between nightmare and void chest islands
    const val DISTANCE_BETWEEN_ISLANDS = 1000
}