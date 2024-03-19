package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

object ModBiomes {
    val EERIE_FOREST = ResourceKey.create(Registry.BIOME_REGISTRY, ResourceLocation(Constants.MOD_ID, "eerie_forest"))
    val NIGHTMARE = ResourceKey.create(Registry.BIOME_REGISTRY, ResourceLocation(Constants.MOD_ID, "nightmare"))
    val VOID_CHEST = ResourceKey.create(Registry.BIOME_REGISTRY, ResourceLocation(Constants.MOD_ID, "void_chest"))

    val LIST = arrayOf(
        EERIE_FOREST,
        NIGHTMARE,
        VOID_CHEST
    )
}