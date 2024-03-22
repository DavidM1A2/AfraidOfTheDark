package com.davidm1a2.afraidofthedark.common.item.igneous

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDShieldItem
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import kotlin.math.max

class IgneousShieldItem : AOTDShieldItem("igneous_shield", Properties()) {
    override fun canBeDepleted(): Boolean {
        return false
    }

    override fun use(world: Level, playerEntity: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        return if (playerEntity.getResearch().isResearched(ModResearches.IGNEOUS)) {
            super.use(world, playerEntity, hand)
        } else {
            InteractionResultHolder.fail(playerEntity.getItemInHand(hand))
        }
    }

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<Component>, iTooltipFlag: TooltipFlag) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            tooltip.add(TranslatableComponent(LocalizationConstants.TOOLTIP_MAGIC_ITEM_NEVER_BREAK))
        } else {
            tooltip.add(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
        }
    }

    override fun onBlock(entity: Player, damageSource: DamageSource) {
        val dmgSourceEntity = damageSource.entity
        if (dmgSourceEntity != null) {
            dmgSourceEntity.remainingFireTicks = max(dmgSourceEntity.remainingFireTicks, 40)

            val direction = dmgSourceEntity.position()
                .subtract(entity.position())
                .normalize()
                .scale(KNOCKBACK_STRENGTH)

            // Move the entity away from the player
            dmgSourceEntity.push(
                direction.x,
                direction.y,
                direction.z
            )
        }
    }

    companion object {
        // How much strength the armor knocks back enemies that attack you. It's roughly the number of blocks to push
        private const val KNOCKBACK_STRENGTH = 2.0
    }
}