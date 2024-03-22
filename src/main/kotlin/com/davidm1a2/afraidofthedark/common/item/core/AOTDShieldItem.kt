package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ShieldItem
import net.minecraft.world.level.Level
import net.minecraftforge.common.ToolAction
import net.minecraftforge.common.ToolActions

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

    override fun getProperties(): List<Pair<ResourceLocation, ClampedItemPropertyFunction>> {
        return listOf(
            // Emit a blocking = 1 property when blocked, 0 otherwise
            ResourceLocation(Constants.MOD_ID, "blocking") to ClampedItemPropertyFunction { stack: ItemStack, _: Level?, livingEntity: LivingEntity?, _ ->
                if (livingEntity != null && livingEntity.isUsingItem && livingEntity.useItem == stack) 1.0f else 0.0f
            }
        )
    }

    override fun canPerformAction(stack: ItemStack, toolAction: ToolAction): Boolean {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction)
    }

    override fun isValidRepairItem(itemStack: ItemStack, repairItemStack: ItemStack): Boolean {
        return false
    }

    open fun onBlock(entity: Player, damageSource: DamageSource) {}
}