package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.util.RegistryKey
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry

object ModBiomes {
    val EERIE_FOREST = RegistryKey.create(Registry.BIOME_REGISTRY, ResourceLocation(Constants.MOD_ID, "eerie_forest"))
    val NIGHTMARE = RegistryKey.create(Registry.BIOME_REGISTRY, ResourceLocation(Constants.MOD_ID, "nightmare"))
    val VOID_CHEST = RegistryKey.create(Registry.BIOME_REGISTRY, ResourceLocation(Constants.MOD_ID, "void_chest"))

    val LIST = arrayOf(
        EERIE_FOREST,
        NIGHTMARE,
        VOID_CHEST
    )
}