package com.davidm1a2.afraidofthedark.common.item.igneous

import com.davidm1a2.afraidofthedark.client.item.igneousShield.IgneousShieldItemStackRenderer
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDShieldItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.world.entity.player.Player
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.DamageSource
import net.minecraft.util.Hand
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.level.Level
import java.util.concurrent.Callable
import kotlin.math.max

class IgneousShieldItem : AOTDShieldItem("igneous_shield", Properties().setISTER { Callable { IgneousShieldItemStackRenderer() } }) {
    override fun canBeDepleted(): Boolean {
        return false
    }

    override fun use(world: Level, playerEntity: Player, hand: Hand): ActionResult<ItemStack> {
        return if (playerEntity.getResearch().isResearched(ModResearches.IGNEOUS)) {
            super.use(world, playerEntity, hand)
        } else {
            ActionResult.fail(playerEntity.getItemInHand(hand))
        }
    }

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_MAGIC_ITEM_NEVER_BREAK))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
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