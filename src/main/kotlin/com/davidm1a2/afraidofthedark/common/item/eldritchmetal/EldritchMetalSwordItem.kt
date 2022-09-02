package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources.getSilverDamage
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSwordItem
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class EldritchMetalSwordItem : AOTDSwordItem("eldritch_metal_sword", ModToolMaterials.ELDRITCH_METAL, 3, -2.4f, Properties()) {
    override fun hurtEnemy(stack: ItemStack, enemy: LivingEntity, hittingEntity: LivingEntity): Boolean {
        // If the player has researched an unsettling material then do silver damage
        if (hittingEntity is PlayerEntity) {
            if (hittingEntity.getResearch().isResearched(ModResearches.AN_UNSETTLING_MATERIAL)) {
                enemy.hurt(getSilverDamage(hittingEntity), damage)
            } else {
                return true
            }
        }

        // Otherwise do standard entity damage
        return super.hurtEnemy(stack, enemy, hittingEntity)
    }
}