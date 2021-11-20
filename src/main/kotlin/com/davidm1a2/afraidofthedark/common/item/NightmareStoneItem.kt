package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.item.ItemStack

/**
 * Class representing nightmare stone
 *
 * @constructor initializes the item name
 */
class NightmareStoneItem : AOTDItem("nightmare_stone", Properties()) {
    override fun hasContainerItem(stack: ItemStack?): Boolean {
        return true
    }

    override fun getContainerItem(itemStack: ItemStack?): ItemStack {
        return ItemStack(this, 1)
    }
}