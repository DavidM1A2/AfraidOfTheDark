package com.davidm1a2.afraidofthedark.common.item.crossbow

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import com.davidm1a2.afraidofthedark.common.item.core.AOTDPerItemCooldownItem
import com.davidm1a2.afraidofthedark.common.item.core.IHasModelProperties
import com.davidm1a2.afraidofthedark.common.network.packets.other.FireWristCrossbowPacket
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

/**
 * Class representing a wrist-mounted crossbow
 *
 * @constructor sets up item properties
 */
class WristCrossbowItem : AOTDPerItemCooldownItem("wrist_crossbow", Properties()), IHasModelProperties {
    override fun getProperties(): List<Pair<ResourceLocation, ClampedItemPropertyFunction>> {
        return listOf(
            ResourceLocation(Constants.MOD_ID, "is_loaded") to ClampedItemPropertyFunction { stack, _, _, _ ->
                if (isOnCooldown(stack)) 0f else 1f
            }
        )
    }

    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = player.getItemInHand(hand)
        if (world.isClientSide) {
            val shot = shoot(player)
            return if (shot) {
                InteractionResultHolder.success(itemStack)
            } else {
                InteractionResultHolder.fail(itemStack)
            }
        }

        return InteractionResultHolder.consume(itemStack)
    }

    fun shoot(player: Player): Boolean {
        if (!player.getResearch().isResearched(ModResearches.WRIST_CROSSBOW)) {
            player.sendMessage(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
            return false
        }

        if (!player.inventory.contains(ItemStack(ModItems.WRIST_CROSSBOW))) {
            player.sendMessage(TranslatableComponent("message.afraidofthedark.wrist_crossbow.no_crossbow"))
            return false
        }

        // Grab the currently selected bolt type
        val ammo = findAmmo(player)
        if (ammo == null) {
            player.sendMessage(TranslatableComponent("message.afraidofthedark.wrist_crossbow.no_bolt"))
            return false
        }

        // Find the wrist crossbow item in the player's inventory
        for (itemStack in player.inventory.items) {
            if (itemStack.item == this) {
                // Test if the crossbow is on CD or not. If it is fire, if it is not continue searching
                if (!isOnCooldown(itemStack)) {
                    // Tell the server to fire the crossbow
                    AfraidOfTheDark.packetHandler.sendToServer(FireWristCrossbowPacket(ammo))
                    // Set the item on CD
                    setOnCooldown(itemStack, player)
                    // Return, we fired the bolt
                    return true
                }
            }
        }
        // No valid wrist crossbow found
        player.sendMessage(TranslatableComponent("message.afraidofthedark.wrist_crossbow.reloading"))

        return false
    }

    private fun findAmmo(player: Player): AOTDBoltItem? {
        for (itemStack in player.inventory.items) {
            val item = itemStack.item
            if (item is AOTDBoltItem) {
                return item
            }
        }

        return if (player.isCreative) {
            ModItems.WOODEN_BOLT
        } else {
            null
        }
    }

    /**
     * Adds tooltip text to the item
     *
     * @param stack   The itemstack to add text to
     * @param world The world that this item is in
     * @param tooltip The tooltip to add to
     * @param flag  The flag telling us if advanced tooltips are on or off
     */
    override fun appendHoverText(stack: ItemStack, world: Level?, tooltip: MutableList<Component>, flag: TooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && player.getResearch().isResearched(ModResearches.WRIST_CROSSBOW)) {
            tooltip.add(
                TranslatableComponent(
                    "tooltip.afraidofthedark.wrist_crossbow.how_to_fire",
                    ModKeybindings.FIRE_WRIST_CROSSBOW.translatedKeyMessage
                )
            )
        } else {
            tooltip.add(TranslatableComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    override fun getCooldownInMilliseconds(itemStack: ItemStack): Int {
        return 3000
    }
}