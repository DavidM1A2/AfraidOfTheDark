/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

/**
 * References for static final variables
 */
object Constants
{
    // The ID of the mod
    const val MOD_ID = "afraidofthedark"
    // The mod name
    const val MOD_NAME = "Afraid of the Dark"
    // The mod version
    const val MOD_VERSION = "1.2.0"
    // The minecraft version number
    const val MC_VERSION = "1.12.2"
    // Refrences to the proxies
    const val SERVER_PROXY_CLASS = "com.davidm1a2.afraidofthedark.proxy.ServerProxy"
    const val CLIENT_PROXY_CLASS = "com.davidm1a2.afraidofthedark.proxy.ClientProxy"
    // Reference to the GUI factory class to translate from IDs to UI objects
    const val GUI_FACTORY_CLASS = "com.davidm1a2.afraidofthedark.client.gui.AOTDGuiFactory"

    // Creative Tab for the mod
    val AOTD_CREATIVE_TAB: CreativeTabs = object : CreativeTabs(MOD_ID)
    {
        /**
         * Getter for the mod creative tab icon
         *
         * @return The icon for the creative tab as an item
         */
        override fun getTabIconItem(): ItemStack
        {
            return ItemStack(ModItems.JOURNAL)
        }
    }

    // The base size of all GUIs
    const val GUI_WIDTH = 640
    const val GUI_HEIGHT = 360

    // All text will be rendered with this scale factor to avoid blurry text
    const val TEXT_SCALE_FACTOR = 0.25f
}