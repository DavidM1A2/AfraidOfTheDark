package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

/**
 * Class representing the telescope item used to track meteors
 *
 * @constructor sets up item properties
 */
class ItemTelescope : AOTDItem("telescope")
{
    /**
     * Called when the player right clicks with the telescope
     *
     * @param world  The world that the telescope was right clicked in
     * @param player The player that right clicked the telescope
     * @param hand   The hand the telescope is in
     * @return The result of the right click
     */
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>
    {
        // Grab the itemstack the player is holding
        val itemStack = player.getHeldItem(hand)
        // Grab the player's research
        val playerResearch = player.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!
        // Test if the player is high enough to use the telescope
        val highEnough = player.position.y > 128

        // Start with server side processing
        if (!world.isRemote)
        {
            // If the player can research the research research it
            if (playerResearch.canResearch(ModResearches.ASTRONOMY_1) && highEnough)
            {
                playerResearch.setResearch(ModResearches.ASTRONOMY_1, true)
                playerResearch.sync(player, true)
            }

            // If the research is researched then test if the player is high enough
            if (playerResearch.isResearched(ModResearches.ASTRONOMY_1) || playerResearch.isResearched(ModResearches.ASTRONOMY_1.preRequisite!!))
            {
                // Tell the player that they need to be higher to see through the clouds
                if (!highEnough)
                {
                    player.sendMessage(TextComponentTranslation("aotd.telescope.not_high_enough"))
                }
            }
            else
            {
                player.sendMessage(TextComponentTranslation("aotd.dont_understand"))
            }
        }

        // If we're on client side and have the proper research and the player is above y=128 to see the stars, show the GUI
        // Don't print anything out client side since the server side takes care of that for us
        if (world.isRemote && highEnough)
        {
            if (playerResearch.isResearched(ModResearches.ASTRONOMY_1) || playerResearch.canResearch(ModResearches.ASTRONOMY_1))
            {
                player.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.TELESCOPE_ID, world, player.position.x, player.position.y, player.position.z)
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack)
    }
}