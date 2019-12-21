package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemWithSharedCooldown
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * Cloak of agility item used to dash around
 *
 * @constructor sets up item properties
 */
class ItemCloakOfAgility : AOTDItemWithSharedCooldown("cloak_of_agility")
{
    /**
     * Called to add a tooltip to the item
     *
     * @param stack   The itemstack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip to add to
     * @param flag  True if the advanced flag is set or false otherwise
     */
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag)
    {
        val player = Minecraft.getMinecraft().player
        // If the player has the research show them what key is used to roll, otherwise tell them they don't know how to use the cloak
        if (player != null && player.getResearch().isResearched(ModResearches.CLOAK_OF_AGILITY))
        {
            tooltip.add("Use " + ROLL_WITH_CLOAK_OF_AGILITY.displayName + " to perform a roll in")
            tooltip.add("the current direction of movement")
        }
        else
        {
            tooltip.add("I'm not sure how to use this.")
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    override fun getItemCooldownInMilliseconds(itemStack: ItemStack): Int
    {
        return 4000
    }
}