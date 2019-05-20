package com.DavidM1A2.afraidofthedark.common.item.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Special version of the per-item cooldown class that ensures all items share a cooldown instead of all having unique cooldowns
 */
public abstract class AOTDItemWithSharedCooldown extends AOTDItemWithPerItemCooldown
{
    /**
     * Constructor sets up item properties
     *
     * @param baseName The name of the item to be used by the game registry
     */
    public AOTDItemWithSharedCooldown(String baseName)
    {
        super(baseName);
    }

    /**
     * Set all similar items on cooldown as well
     *
     * @param itemStack    The item to set on cooldown
     * @param entityPlayer The player that is holding the item
     */
    @Override
    public void setOnCooldown(ItemStack itemStack, EntityPlayer entityPlayer)
    {
        // For all items in the player's inventory that are of the same item set them on cooldown as well
        for (ItemStack newStack : entityPlayer.inventory.mainInventory)
            if (itemStack.getItem().equals(newStack.getItem()))
            {
                super.setOnCooldown(newStack, entityPlayer);
            }
    }
}
