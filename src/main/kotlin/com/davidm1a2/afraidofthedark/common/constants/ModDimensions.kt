package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

object ModDimensions {
    val NIGHTMARE_WORLD = ResourceKey.create(Registry.DIMENSION_REGISTRY, ResourceLocation(Constants.MOD_ID, "nightmare"))!!
    val VOID_CHEST_WORLD = ResourceKey.create(Registry.DIMENSION_REGISTRY, ResourceLocation(Constants.MOD_ID, "void_chest"))!!
}