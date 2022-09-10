package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.LivingEntity
import net.minecraft.item.IItemPropertyGetter
import net.minecraft.item.ItemStack
import net.minecraft.item.ShieldItem
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

abstract class AOTDShieldItem(
    baseName: String,
    properties: Properties,
    displayInCreative: Boolean = true
) : ShieldItem(properties.apply {
    if (displayInCreative) {
        tab(Constants.AOTD_CREATIVE_TAB)
    }
}), IHasModelProperties {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }

    override fun getProperties(): List<Pair<ResourceLocation, IItemPropertyGetter>> {
        return listOf(
            // Emit a blocked = 1 property when blocked, 0 otherwise
            ResourceLocation(Constants.MOD_ID, "blocked") to IItemPropertyGetter { stack: ItemStack, _: World?, livingEntity: LivingEntity? ->
                if (livingEntity != null && livingEntity.isUsingItem && livingEntity.useItem == stack) 1.0f else 0.0f
            }
        )
    }

    override fun isShield(stack: ItemStack, entity: LivingEntity?): Boolean {
        return true
    }

    override fun isValidRepairItem(itemStack: ItemStack, repairItemStack: ItemStack): Boolean {
        return false
    }
}