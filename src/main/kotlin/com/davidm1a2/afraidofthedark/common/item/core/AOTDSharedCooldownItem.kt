package com.davidm1a2.afraidofthedark.common.item.core

import net.minecraft.world.entity.player.Player
import net.minecraft.item.ItemStack

/**
 * Special version of the per-item cooldown class that ensures all items share a cooldown instead of all having unique cooldowns
 *
 * @constructor sets up item properties
 * @param baseName The name of the item to be used by the game registry
 */
abstract class AOTDSharedCooldownItem(
    baseName: String,
    properties: Properties,
    displayInCreative: Boolean = true
) : AOTDPerItemCooldownItem(baseName, properties, displayInCreative) {
    /**
     * Set all similar items on cooldown as well
     *
     * @param itemStack    The item to set on cooldown
     * @param entityPlayer The player that is holding the item
     */
    override fun setOnCooldown(itemStack: ItemStack, entityPlayer: Player) {
        // For all items in the player's inventory that are of the same item set them on cooldown as well
        for (newStack in entityPlayer.inventory.items + entityPlayer.inventory.offhand) {
            if (itemStack.item == newStack.item) {
                super.setOnCooldown(newStack, entityPlayer)
            }
        }
    }
}