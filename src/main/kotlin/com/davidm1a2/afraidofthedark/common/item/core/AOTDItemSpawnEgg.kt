package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.EntityType
import net.minecraft.item.ItemSpawnEgg

abstract class AOTDItemSpawnEgg(
    name: String,
    entityType: EntityType<*>,
    primaryColor: Int,
    secondaryColor: Int,
    properties: Properties
) : ItemSpawnEgg(entityType, primaryColor, secondaryColor, properties.apply {
    group(Constants.AOTD_CREATIVE_TAB)
}) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }
}