package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.utility.openGui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

/**
 * Class representing the telescope item used to track meteors
 */
class ItemSextant : AOTDItem("sextant")
{
    /**
     * Called when the player right clicks with the sextant
     *
     * @param world  The world that the sextant was right clicked in
     * @param player The player that right clicked the sextant
     * @param hand   The hand the telescope is in
     * @return The result of the right click
     */
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>
    {
        val itemStack = player.getHeldItem(hand)
        val playerResearch = player.getResearch()

        // If the player has astronomy 1 open the GUI client side
        if (playerResearch.isResearched(ModResearches.ASTRONOMY_1))
        {
            // Only open GUIs client side
            if (world.isRemote)
            {
                player.openGui(AOTDGuiHandler.SEXTANT_ID)
            }
        }
        // If the player does not have the research send him a chat message from the server
        else
        {
            if (!world.isRemote)
            {
                player.sendMessage(TextComponentTranslation("message.afraidofthedark:dont_understand"))
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack)
    }
}