package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSharedCooldownItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

/**
 * Cloak of agility item used to dash around
 *
 * @constructor sets up item properties
 */
class CloakOfAgilityItem : AOTDSharedCooldownItem("cloak_of_agility", Properties()) {
    /**
     * Called to add a tooltip to the item
     *
     * @param stack   The itemstack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip to add to
     * @param flag  True if the advanced flag is set or false otherwise
     */
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        // If the player has the research show them what key is used to roll, otherwise tell them they don't know how to use the cloak
        if (player != null && player.getResearch().isResearched(ModResearches.CLOAK_OF_AGILITY)) {
            tooltip.add(
                TranslationTextComponent(
                    "tooltip.afraidofthedark.cloak_of_agility.line1",
                    ROLL_WITH_CLOAK_OF_AGILITY.localizedName
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
}