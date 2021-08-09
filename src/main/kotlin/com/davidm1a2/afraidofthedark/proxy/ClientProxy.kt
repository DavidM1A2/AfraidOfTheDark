package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.ReadBookScreen
import net.minecraft.client.gui.screen.ReadBookScreen.WrittenBookInfo
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.StringNBT

/**
 * Proxy that is only to be instantiated on the CLIENT side
 */
class ClientProxy : IProxy {
    override fun showInsanitysHeightsBook() {
        // A hint book itemstack used purely to open the book GUI, it's never actually given to the player
        val hintBook = createHintBook()
        Minecraft.getInstance().setScreen(ReadBookScreen(WrittenBookInfo(hintBook)))
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