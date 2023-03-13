package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.IItemPropertyGetter
import net.minecraft.item.ItemStack
import net.minecraft.item.ShieldItem
import net.minecraft.util.DamageSource
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
            // Emit a blocking = 1 property when blocked, 0 otherwise
            ResourceLocation(Constants.MOD_ID, "blocking") to IItemPropertyGetter { stack: ItemStack, _: World?, livingEntity: LivingEntity? ->
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

    open fun onBlock(entity: PlayerEntity, damageSource: DamageSource) {}
}