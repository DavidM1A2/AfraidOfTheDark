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

    // The reference UI square size used for any calculations that need it
    const val REFERENCE_SIZE = 480f

    // All text will be rendered with this scale factor to avoid blurry text
    const val TEXT_SCALE_FACTOR = 0.4f

    // The distance between nightmare and void chest islands
    const val DISTANCE_BETWEEN_ISLANDS = 1000
}