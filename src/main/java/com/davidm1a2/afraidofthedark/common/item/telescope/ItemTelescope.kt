package com.davidm1a2.afraidofthedark.common.item.telescope

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import com.davidm1a2.afraidofthedark.common.utility.openGui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

/**
 * Basic telescope item used to track meteors. Has an accuracy of 130 blocks
 */
class ItemTelescope : ItemTelescopeBase(130, "telescope") {
    /**
     * Called when the player right clicks with the telescope
     *
     * @param world  The world that the telescope was right clicked in
     * @param player The player that right clicked the telescope
     * @param hand   The hand the telescope is in
     * @return The result of the right click
     */
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        // Test if the player is high enough to use the telescope
        val highEnough = player.position.y > 128

        // Grab the player's research
        val playerResearch = player.getResearch()

        if (!world.isRemote) {
            // If the player can research the research research it
            if (playerResearch.canResearch(ModResearches.ASTRONOMY_1) && highEnough) {
                playerResearch.setResearch(ModResearches.ASTRONOMY_1, true)
                playerResearch.sync(player, true)
            }
        }

        // Also allow showing the gui if the player can research the telescope research
        if (world.isRemote && highEnough) {
            if (playerResearch.canResearch(getRequiredResearch())) {
                player.openGui(AOTDGuiHandler.TELESCOPE_ID)
            }
        }

        return super.onItemRightClick(world, player, hand)
    }

    /**
     * Gets the required research to use this item
     *
     * @return A research
     */
    override fun getRequiredResearch(): Research {
        return ModResearches.ASTRONOMY_1
    }
}