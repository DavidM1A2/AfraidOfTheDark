package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier

abstract class AOTDResearchRequiredSwordItem(
    baseName: String,
    toolMaterial: Tier,
    damageAmplifier: Int,
    attackSpeed: Float,
    protected val requiredResearch: Research,
    properties: Properties,
    displayInCreative: Boolean = true
) : AOTDSwordItem(baseName, toolMaterial, damageAmplifier, attackSpeed, properties, displayInCreative) {
    override fun onLeftClickEntity(stack: ItemStack, player: Player, target: Entity): Boolean {
        if (!player.getResearch().isResearched(requiredResearch)) {
            return true
        }

        return super.onLeftClickEntity(stack, player, target)
    }
}