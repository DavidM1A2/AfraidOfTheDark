package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.client.gui.screens.SextantScreen
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

/**
 * Class representing the telescope item used to track meteors
 */
class SextantItem : AOTDItem("sextant", Properties().maxStackSize(1)) {
    /**
     * Called when the player right clicks with the sextant
     *
     * @param world  The world that the sextant was right clicked in
     * @param player The player that right clicked the sextant
     * @param hand   The hand the telescope is in
     * @return The result of the right click
     */
    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand)
        val playerResearch = player.getResearch()

        // If the player has astronomy 1 open the GUI client side
        if (playerResearch.isResearched(ModResearches.ASTRONOMY_1)) {
            // Only open GUIs client side
            if (world.isRemote) {
                Minecraft.getInstance().displayGuiScreen(SextantScreen())
            }
        }
        // If the player does not have the research send him a chat message from the server
        else {
            if (!world.isRemote) {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }
        return ActionResult.resultSuccess(itemStack)
    }
}