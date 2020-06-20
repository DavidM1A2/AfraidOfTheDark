package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockLog
import net.minecraft.block.SoundType
import net.minecraft.block.material.MaterialColor

/**
 * Base class for all AOTD log blocks
 *
 * @constructor just sets default state and initializes sounds
 * @param baseName The name of the block to register
 * @param materialColor The material color of the log
 * @param properties The properties of this block
 */
abstract class AOTDBlockLog(baseName: String, materialColor: MaterialColor, properties: Properties) : BlockLog(materialColor, properties.apply {
    hardnessAndResistance(2.0f)
    sound(SoundType.WOOD)
}), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }

    fun getRawMaterialColor(): MaterialColor {
        return blockMapColor
    }
}