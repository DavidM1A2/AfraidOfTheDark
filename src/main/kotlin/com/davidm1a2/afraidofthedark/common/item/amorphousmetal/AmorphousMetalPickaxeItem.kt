package com.davidm1a2.afraidofthedark.common.item.amorphousmetal

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredPickaxeItem
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class AmorphousMetalPickaxeItem : AOTDResearchRequiredPickaxeItem("amorphous_metal_pickaxe", ModToolMaterials.AMORPHOUS_METAL, 1, -2.8f, ModResearches.CATALYSIS, Properties()) {
    override fun onLeftClickEntity(stack: ItemStack, player: PlayerEntity, target: Entity): Boolean {
        if (player.getResearch().isResearched(requiredResearch)) {
            target.hurt(ModDamageSources.getSilverDamage(player), attackDamage)
            return false
        }

        return super.onLeftClickEntity(stack, player, target)
    }
}
