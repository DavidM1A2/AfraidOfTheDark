package com.davidm1a2.afraidofthedark.common.item.telescope

import com.davidm1a2.afraidofthedark.client.gui.screens.TelescopeScreen
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Basic telescope item used to track meteors. Has an accuracy of 130 blocks
 */
class TelescopeItem : TelescopeBaseItem(130, "telescope") {
    /**
     * Called when the player right clicks with the telescope
     *
     * @param world  The world that the telescope was right clicked in
     * @param player The player that right clicked the telescope
     * @param hand   The hand the telescope is in
     * @return The result of the right click
     */
    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        // Grab the itemstack the player is holding
        val itemStack = player.getHeldItem(hand)

        // Test if the player is high enough to use the telescope
        val highEnough = player.position.y > 128

        // Grab the player's research
        val playerResearch = player.getResearch()

        // The research required
        val research = getRequiredResearch()

        if (!world.isRemote) {
            // If the player can research the research research it
            if (playerResearch.canResearch(ModResearches.ASTRONOMY_1) && highEnough) {
                playerResearch.setResearch(ModResearches.ASTRONOMY_1, true)
                playerResearch.sync(player, true)
            }

            // If the research is researched then test if the player is high enough
            if (playerResearch.isResearched(research) || playerResearch.canResearch(research)) {
                // Tell the player that they need to be higher to see through the clouds
                if (!highEnough) {
                    player.sendMessage(TranslationTextComponent("message.afraidofthedark.telescope.not_high_enough"))
                }
            } else {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }

        // Also allow showing the gui if the player can research the telescope research
        if (world.isRemote && highEnough) {
            if (playerResearch.isResearched(research) || playerResearch.canResearch(research)) {
                Minecraft.getInstance().displayGuiScreen(TelescopeScreen())
            }
        }

        return ActionResult.newResult(ActionResultType.SUCCESS, itemStack)
    }

    /**
     * Gets the required research to use this item
     *
     * @return A research
     */
    override fun getRequiredResearch(): Research {
        return ModResearches.ASTRONOMY_1
    }

    /**
     * Adds a tooltip to the telescope item
     *
     * @param stack   The stack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip to add to
     * @param flag  True if the advanced tooltip is set on, false otherwise
     */
    @OnlyIn(Dist.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        // Show the tooltip if the pre-req is researched
        if (Minecraft.getInstance().player?.getResearch()?.isResearched(getRequiredResearch().preRequisite!!) == true) {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.telescope.directions"))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.telescope.accuracy", accuracy))
        } else {
            super.addInformation(stack, world, tooltip, flag)
        }
    }
}