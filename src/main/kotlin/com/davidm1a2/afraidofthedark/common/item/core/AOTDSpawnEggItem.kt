package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.EntityType
import net.minecraft.item.SpawnEggItem

abstract class AOTDSpawnEggItem(
    name: String,
    entityType: EntityType<*>,
    primaryColor: Int,
    secondaryColor: Int,
    properties: Properties
) : SpawnEggItem(entityType, primaryColor, secondaryColor, properties.apply {
    tab(Constants.AOTD_CREATIVE_TAB)
}) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }
}