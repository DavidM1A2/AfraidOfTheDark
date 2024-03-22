package com.davidm1a2.afraidofthedark.common.item.amorphousmetal

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredHoeItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.level.Level

class AmorphousMetalHoeItem : AOTDResearchRequiredHoeItem("amorphous_metal_hoe", ModToolMaterials.AMORPHOUS_METAL, -7, 0.0f, ModResearches.CATALYSIS, Properties()) {
    override fun onLeftClickEntity(stack: ItemStack, player: Player, target: Entity): Boolean {
        if (player.getResearch().isResearched(requiredResearch)) {
            target.hurt(ModDamageSources.getSilverDamage(player), attackDamage)
            return false
        }

        return super.onLeftClickEntity(stack, player, target)
    }

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.amorphous_metal_tool.double_drops"))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.amorphous_metal_tool.no_drops"))
        }
    }
}
