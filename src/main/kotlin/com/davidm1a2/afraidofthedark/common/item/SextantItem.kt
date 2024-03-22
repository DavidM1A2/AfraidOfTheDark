package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.client.gui.screens.SextantScreen
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.Player
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.level.Level

/**
 * Class representing the telescope item used to track meteors
 */
class SextantItem : AOTDItem("sextant", Properties().stacksTo(1)) {
    /**
     * Called when the player right clicks with the sextant
     *
     * @param world  The world that the sextant was right clicked in
     * @param player The player that right clicked the sextant
     * @param hand   The hand the telescope is in
     * @return The result of the right click
     */
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = player.getItemInHand(hand)
        val playerResearch = player.getResearch()

        // If the player has astronomy 1 open the GUI client side
        if (playerResearch.isResearched(ModResearches.ASTRONOMY_1)) {
            // Only open GUIs client side
            if (world.isClientSide) {
                Minecraft.getInstance().setScreen(SextantScreen())
            }
        }
        // If the player does not have the research send him a chat message from the server
        else {
            if (!world.isClientSide) {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }
        return ActionResult.success(itemStack)
    }
}