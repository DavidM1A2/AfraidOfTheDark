package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSharedCooldownItem
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.world.entity.player.Player
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.level.Level

/**
 * Cloak of agility item used to dash around
 *
 * @constructor sets up item properties
 */
class CloakOfAgilityItem : AOTDSharedCooldownItem("cloak_of_agility", Properties()) {
    override fun use(world: Level, playerEntity: Player, hand: Hand): ActionResult<ItemStack> {
        val itemStack = playerEntity.getItemInHand(hand)
        if (world.isClientSide) {
            val wasSuccess = roll(playerEntity, itemStack)
            return if (wasSuccess) {
                ActionResult.success(itemStack)
            } else {
                ActionResult.fail(itemStack)
            }
        }

        return ActionResult.consume(itemStack)
    }

    fun roll(player: Player, cloakStack: ItemStack): Boolean {
        if (!player.getResearch().isResearched(ModResearches.CLOAK_OF_AGILITY)) {
            player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            return false
        }

        if (!player.isOnGround) {
            player.sendMessage(TranslationTextComponent("message.afraidofthedark.cloak_of_agility.not_grounded"))
            return false
        }

        if (isOnCooldown(cloakStack)) {
            player.sendMessage(TranslationTextComponent("message.afraidofthedark.cloak_of_agility.too_tired", cooldownRemainingInSeconds(cloakStack)))
            return false
        }

        // Set the cloak on CD
        setOnCooldown(cloakStack, player)

        // If the player is not moving roll in the direction the player is looking, otherwise roll in the direction the player is moving
        var motionDirection = if (player.deltaMovement.x <= 0.01 && player.deltaMovement.x >= -0.01 && player.deltaMovement.z <= 0.01 && player.deltaMovement.z >= -0.01) {
            val lookDirection = player.lookAngle
            Vector3d(lookDirection.x, 0.0, lookDirection.z)
        } else {
            Vector3d(player.deltaMovement.x, 0.0, player.deltaMovement.z)
        }

        // Normalize the motion vector
        motionDirection = motionDirection.normalize()

        // Update the player's motion in the new direction
        player.setDeltaMovement(
            motionDirection.x * ROLL_VELOCITY,
            0.2,
            motionDirection.z * ROLL_VELOCITY
        )

        return true
    }

    /**
     * Called to add a tooltip to the item
     *
     * @param stack   The itemstack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip to add to
     * @param flag  True if the advanced flag is set or false otherwise
     */
    override fun appendHoverText(stack: ItemStack, world: Level?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        // If the player has the research show them what key is used to roll, otherwise tell them they don't know how to use the cloak
        if (player != null && player.getResearch().isResearched(ModResearches.CLOAK_OF_AGILITY)) {
            tooltip.add(
                TranslationTextComponent(
                    "tooltip.afraidofthedark.cloak_of_agility.line1",
                    ROLL_WITH_CLOAK_OF_AGILITY.translatedKeyMessage
                )
            )
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.cloak_of_agility.line2"))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    override fun getCooldownInMilliseconds(itemStack: ItemStack): Int {
        return 4000
    }

    companion object {
        private const val ROLL_VELOCITY = 3.0
    }
}