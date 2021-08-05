package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.util.RegistryKey
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry

object ModDimensions {
    val NIGHTMARE_WORLD = RegistryKey.create(Registry.DIMENSION_REGISTRY, ResourceLocation(Constants.MOD_ID, "nightmare"))!!
    val VOID_CHEST_WORLD = RegistryKey.create(Registry.DIMENSION_REGISTRY, ResourceLocation(Constants.MOD_ID, "void_chest"))!!
}