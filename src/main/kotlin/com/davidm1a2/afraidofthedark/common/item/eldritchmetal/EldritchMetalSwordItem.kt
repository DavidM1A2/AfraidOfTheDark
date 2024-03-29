package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredSwordItem
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class EldritchMetalSwordItem : AOTDResearchRequiredSwordItem("eldritch_metal_sword", ModToolMaterials.ELDRITCH_METAL, 3, -2.4f, ModResearches.AN_UNSETTLING_MATERIAL, Properties()), IEldritchItem {
    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        EldritchMetalCommons.processItem(itemStack, world, entity)
    }

    override fun onLeftClickEntity(stack: ItemStack, player: PlayerEntity, target: Entity): Boolean {
        if (player.getResearch().isResearched(requiredResearch)) {
            target.hurt(ModDamageSources.getSilverDamage(player), damage)
            return false
        }

        return super.onLeftClickEntity(stack, player, target)
    }
}