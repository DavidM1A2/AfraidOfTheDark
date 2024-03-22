package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraftforge.common.ForgeSpawnEggItem
import java.util.function.Supplier

abstract class AOTDSpawnEggItem(
    name: String,
    entityType: Supplier<out EntityType<out Mob>>,
    primaryColor: Int,
    secondaryColor: Int,
    properties: Properties
) : ForgeSpawnEggItem(entityType, primaryColor, secondaryColor, properties.apply {
    tab(Constants.AOTD_CREATIVE_TAB)
}) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }
}