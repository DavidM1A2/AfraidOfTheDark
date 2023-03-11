package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.Item
import net.minecraft.item.MusicDiscItem
import net.minecraft.util.SoundEvent

/**
 * Base class for all AOTD items
 *
 * @constructor sets up item properties
 * @param baseName The name of the item to be used by the game registry
 * @param displayInCreative True if the item should show up in creative, false otherwise
 */
abstract class AOTDMusicDiscItem(
    baseName: String,
    properties: Properties,
    soundEvent: SoundEvent,
    displayInCreative: Boolean = true
) : MusicDiscItem(0, { soundEvent }, properties.apply {
    if (displayInCreative) {
        tab(Constants.AOTD_CREATIVE_TAB)
    }
}) {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}