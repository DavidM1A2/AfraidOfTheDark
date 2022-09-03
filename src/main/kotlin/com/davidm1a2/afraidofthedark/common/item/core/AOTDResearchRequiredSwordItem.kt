package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.IItemTier
import net.minecraft.item.ItemStack

abstract class AOTDResearchRequiredSwordItem(
    baseName: String,
    toolMaterial: IItemTier,
    damageAmplifier: Int,
    attackSpeed: Float,
    protected val requiredResearch: Research,
    properties: Properties,
    displayInCreative: Boolean = true
) : AOTDSwordItem(baseName, toolMaterial, damageAmplifier, attackSpeed, properties, displayInCreative) {
    override fun onLeftClickEntity(stack: ItemStack, player: PlayerEntity, target: Entity): Boolean {
        if (!player.getResearch().isResearched(requiredResearch)) {
            return true
        }

        return super.onLeftClickEntity(stack, player, target)
    }
}