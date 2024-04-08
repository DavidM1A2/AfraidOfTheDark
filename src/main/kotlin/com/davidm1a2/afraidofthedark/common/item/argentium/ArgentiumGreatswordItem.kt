package com.davidm1a2.afraidofthedark.common.item.argentium

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredSwordItem
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class ArgentiumGreatswordItem : AOTDResearchRequiredSwordItem("argentium_greatsword", ModToolMaterials.ARGENTIUM, 3, -1.2f, ModResearches.METALLURGY, Properties()) {
    override fun onLeftClickEntity(stack: ItemStack, player: Player, target: Entity): Boolean {
        if (player.getResearch().isResearched(requiredResearch)) {
            target.hurt(ModDamageSources.getSilverDamage(player), damage)
            return false
        }

        return super.onLeftClickEntity(stack, player, target)
    }
}