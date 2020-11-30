package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.LeavesBlock
import net.minecraft.block.SoundType

/**
 * A base class for any leaves added by the AOTD mod
 *
 * @constructor just requires the name of the block to register as the registry name
 * @param baseName The name to be used by the registry and unlocalized names
 * @param properties The properties of this block
 */
abstract class AOTDLeavesBlock(baseName: String, properties: Properties) : LeavesBlock(properties.apply {
    hardnessAndResistance(0.2f)
    tickRandomly()
    sound(SoundType.PLANT)
}), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}