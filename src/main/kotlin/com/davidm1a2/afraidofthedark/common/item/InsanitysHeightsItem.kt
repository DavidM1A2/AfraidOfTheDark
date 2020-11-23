package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

/**
 * Class representing the "Insanity's Heights" book you get in the nightmare realm
 *
 * @constructor sets the item's name
 */
class InsanitysHeightsItem : AOTDItem("insanitys_heights", Properties().maxStackSize(1)) {
    /**
     * Called when the book is right clicked, opens the book GUI
     *
     * @param worldIn  The world the right click occurred in
     * @param player The player that right clicked
     * @param hand   The hand the player is holding the item in
     * @return Success, the UI opened
     */
    override fun onItemRightClick(worldIn: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val heldItem = player.getHeldItem(hand)
        // Show the player the book if they're in the nightmare
        if (worldIn.dimension.type == ModDimensions.NIGHTMARE_TYPE) {
            AfraidOfTheDark.proxy.showInsanitysHeightsBook()
        } else {
            if (!worldIn.isRemote) {
                player.sendMessage(TranslationTextComponent("message.afraidofthedark.insanitys_heights.dont_understand"))
            }
        }
        return ActionResult.newResult(ActionResultType.SUCCESS, heldItem)
    }
}