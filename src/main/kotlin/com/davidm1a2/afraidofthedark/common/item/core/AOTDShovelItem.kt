package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.Tier

abstract class AOTDShovelItem(
    baseName: String,
    toolMaterial: Tier,
    baseDamage: Float,
    attackSpeedMultiplier: Float,
    properties: Properties,
    displayInCreative: Boolean = true
) : ShovelItem(toolMaterial, baseDamage, attackSpeedMultiplier, properties.apply {
    if (displayInCreative) {
        tab(Constants.AOTD_CREATIVE_TAB)
    }
}) {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}