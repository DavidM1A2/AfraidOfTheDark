package com.davidm1a2.afraidofthedark.common.block.core

import net.minecraft.item.BlockItem

interface IUseCustomBlockItem {
    fun getBlockItem(): BlockItem
}