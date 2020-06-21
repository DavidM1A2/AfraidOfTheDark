package com.davidm1a2.afraidofthedark.common.item.telescope

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Class representing the telescope item used to track meteors
 *
 * @constructor sets up item properties
 * @param accuracy How many blocks away meteors that the telescope finds are dropped
 * @param name The unlocalized name of the item
 */
abstract class ItemTelescopeBase(val accuracy: Int, name: String) : AOTDItem(name, Properties().maxStackSize(1)) {
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
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        // Grab the itemstack the player is holding
        val itemStack = player.getHeldItem(hand)

        // Grab the player's research
        val playerResearch = player.getResearch()

        // Test if the player is high enough to use the telescope
        val highEnough = player.position.y > 128

        // The research required
        val research = getRequiredResearch()

        // Start with server side processing
        if (!world.isRemote) {
            // If the research is researched then test if the player is high enough
            if (playerResearch.isResearched(research)) {
                // Tell the player that they need to be higher to see through the clouds
                if (!highEnough) {
                    player.sendMessage(TextComponentTranslation(LocalizationConstants.Item.TELESCOPE_NOT_HIGH_ENOUGH))
                }
            } else {
                player.sendMessage(TextComponentTranslation(LocalizationConstants.Generic.DONT_UNDERSTAND))
            }
        }

        // If we're on client side and have the proper research and the player is above y=128 to see the stars, show the GUI
        // Don't print anything out client side since the server side takes care of that for us
        if (world.isRemote && highEnough) {
            if (playerResearch.isResearched(research)) {
                // player.openGui(AOTDGuiHandler.TELESCOPE_ID)
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack)
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
        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(getRequiredResearch())) {
            tooltip.add(TextComponentTranslation(LocalizationConstants.Item.TELESCOPE_TOOLTIP_DIRECTIONS))
            tooltip.add(TextComponentTranslation(LocalizationConstants.Item.TELESCOPE_TOOLTIP_ACCURACY, accuracy))
        } else {
            tooltip.add(TextComponentTranslation(LocalizationConstants.Generic.DONT_UNDERSTAND))
        }
    }

    /**
     * Gets the required research to use this item
     *
     * @return A research
     */
    abstract fun getRequiredResearch(): Research
}