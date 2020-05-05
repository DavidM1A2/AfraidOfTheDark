package com.davidm1a2.afraidofthedark.common.item.telescope

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

/**
 * Basic telescope item used to track meteors
 */
class ItemTelescope : ItemTelescopeBase(100, "telescope") {
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val toReturn = super.onItemRightClick(world, player, hand)

        if (!world.isRemote) {
            // Grab the player's research
            val playerResearch = player.getResearch()

            // Test if the player is high enough to use the telescope
            val highEnough = player.position.y > 128

            // If the player can research the research research it
            if (playerResearch.canResearch(ModResearches.ASTRONOMY_1) && highEnough) {
                playerResearch.setResearch(ModResearches.ASTRONOMY_1, true)
                playerResearch.sync(player, true)
            }
        }

        return toReturn
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