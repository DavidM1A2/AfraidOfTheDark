package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.WallBlock

abstract class AOTDWallBlock(baseName: String, properties: Properties) : WallBlock(properties), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}