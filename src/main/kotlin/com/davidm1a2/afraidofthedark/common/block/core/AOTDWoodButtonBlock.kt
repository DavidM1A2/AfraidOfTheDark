package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.WoodButtonBlock

abstract class AOTDWoodButtonBlock(baseName: String, properties: Properties) : WoodButtonBlock(
    properties.apply {
        noCollission()
        strength(0.5f)
    }
), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}