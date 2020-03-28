package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockFenceGate
import net.minecraft.block.BlockPlanks
import net.minecraft.block.SoundType

/**
 * Base class for any fence gate blocks used by Afraid of the dark. Unfortunately the minecraft base class only allows
 * specifying wood type fence gates...
 *
 * @constructor requires a material parameter that defines some block properties
 * @param baseName The name of the block to be used by the game registry
 * @param displayInCreative True if the block should show up in creative, false otherwise
 */
abstract class AOTDBlockFenceGate(
    baseName: String,
    displayInCreative: Boolean = true
) : BlockFenceGate(BlockPlanks.EnumType.OAK) {
    init {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
        this.setHardness(2.0f)
        this.setResistance(5.0f)
        this.soundType = SoundType.WOOD
        if (displayInCreative) {
            setCreativeTab(Constants.AOTD_CREATIVE_TAB)
        }
    }
}