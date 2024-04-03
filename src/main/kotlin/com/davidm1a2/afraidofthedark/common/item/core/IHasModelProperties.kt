package com.davidm1a2.afraidofthedark.common.item.core

import net.minecraft.client.renderer.item.ItemPropertyFunction
import net.minecraft.resources.ResourceLocation

interface IHasModelProperties {
    fun getProperties(): List<Pair<ResourceLocation, ItemPropertyFunction>>
}