package com.davidm1a2.afraidofthedark.common.item.core

import net.minecraft.item.IItemPropertyGetter
import net.minecraft.util.ResourceLocation

interface IHasModelProperties {
    fun getProperties(): List<Pair<ResourceLocation, IItemPropertyGetter>>
}