package com.davidm1a2.afraidofthedark.common.item.telescope

import com.davidm1a2.afraidofthedark.client.gui.screens.TelescopeScreen
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

/**
 * Class representing the telescope item used to track meteors
 *
 * @constructor sets up item properties
 * @param accuracy How many blocks away meteors that the telescope finds are dropped
 * @param name The unlocalized name of the item
 */
abstract class TelescopeBaseItem(val accuracy: Int, name: String) : AOTDItem(name, Properties().stacksTo(1)) {
    init {
        require(accuracy >= 0) {
            "Accuracy for telescopes must be positive!"
        }
    }

    /**
     * Called when the player right clicks with the telescope
     *
     * @param world  The world that the telescope was right clicked in
     * @param player The player that right clicked the telescope
     * @param hand   The hand the telescope is in
     * @return The result of the right click
     */
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        // Grab the itemstack the player is holding
        val itemStack = player.getItemInHand(hand)

        // Grab the player's research
        val playerResearch = player.getResearch()

        // Test if the player is high enough to use the telescope
        val highEnough = player.y > 128

        // The research required
        val research = getRequiredResearch()

        // Start with server side processing
        if (!world.isClientSide) {
            // If the research is researched then test if the player is high enough
            if (playerResearch.isResearched(research)) {
                // Tell the player that they need to be higher to see through the clouds
                if (!highEnough) {
                    player.sendMessage(TranslationTextComponent("message.afraidofthedark.telescope.not_high_enough"), player.uuid)
                }
            } else {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND), player.uuid)
            }
        }

        // If we're on client side and have the proper research and the player is above y=128 to see the stars, show the GUI
        // Don't print anything out client side since the server side takes care of that for us
        if (world.isClientSide && highEnough) {
            if (playerResearch.isResearched(research)) {
                Minecraft.getInstance().setScreen(TelescopeScreen())
            }
        }
        return ActionResult.success(itemStack)
    }

    /**
     * Adds a tooltip to the telescope item
     *
     * @param stack   The stack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip to add to
     * @param flag  True if the advanced tooltip is set on, false otherwise
     */
    override fun appendHoverText(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(getRequiredResearch())) {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.telescope.directions"))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.telescope.accuracy", accuracy))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
        }
    }

    /**
     * Gets the required research to use this item
     *
     * @return A research
     */
    abstract fun getRequiredResearch(): Research
}