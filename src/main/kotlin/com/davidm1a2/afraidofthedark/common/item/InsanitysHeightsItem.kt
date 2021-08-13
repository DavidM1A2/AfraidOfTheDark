package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.ReadBookScreen
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.StringNBT
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

/**
 * Class representing the "Insanity's Heights" book you get in the nightmare realm
 *
 * @constructor sets the item's name
 */
class InsanitysHeightsItem : AOTDItem("insanitys_heights", Properties().stacksTo(1)) {
    /**
     * Called when the book is right clicked, opens the book GUI
     *
     * @param worldIn  The world the right click occurred in
     * @param player The player that right clicked
     * @param hand   The hand the player is holding the item in
     * @return Success, the UI opened
     */
    override fun use(worldIn: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val heldItem = player.getItemInHand(hand)
        // Show the player the book if they're in the nightmare
        if (worldIn.dimension() == ModDimensions.NIGHTMARE_WORLD) {
            if (worldIn.isClientSide) {
                // A hint book itemstack used purely to open the book GUI, it's never actually given to the player
                val hintBook = createHintBook()
                Minecraft.getInstance().setScreen(ReadBookScreen(ReadBookScreen.WrittenBookInfo(hintBook)))
            }
        } else {
            if (!worldIn.isClientSide) {
                player.sendMessage(TranslationTextComponent("message.afraidofthedark.insanitys_heights.dont_understand"), player.uuid)
            }
        }
        return ActionResult.success(heldItem)
    }

    private fun createHintBook(): ItemStack {
        val toReturn = ItemStack(Items.WRITTEN_BOOK, 1)
        NBTHelper.setString(toReturn, "title", I18n.get("nightmarebook.title"))
        NBTHelper.setString(toReturn, "author", I18n.get("nightmarebook.author"))
        NBTHelper.setBoolean(toReturn, "resolved", true)
        toReturn.tag!!.put("pages", createPages())
        return toReturn
    }

    private fun createPages(): ListNBT {
        val pages = ListNBT()
        val bookText = I18n.get("nightmarebook.text").split(";;")
        bookText.forEach {
            pages.add(StringNBT.valueOf(it))
        }
        return pages
    }
}