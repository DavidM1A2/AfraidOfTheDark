package com.davidm1a2.afraidofthedark.common.item.amorphousmetal

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredShovelItem
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class AmorphousMetalShovelItem : AOTDResearchRequiredShovelItem("amorphous_metal_shovel", ModToolMaterials.AMORPHOUS_METAL, 1.5f, -3f, ModResearches.CATALYSIS, Properties()) {
    override fun onLeftClickEntity(stack: ItemStack, player: PlayerEntity, target: Entity): Boolean {
        if (player.getResearch().isResearched(requiredResearch)) {
            target.hurt(ModDamageSources.getSilverDamage(player), attackDamage)
            return false
        }

        return super.onLeftClickEntity(stack, player, target)
    }
}
