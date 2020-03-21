package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.block.core.AOTDDoor
import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.ItemDoor
import net.minecraft.item.ItemStack

/**
 * Base class for all AOTD items
 *
 * @constructor sets up item properties
 * @param door The name of the block this item door will create
 * @param displayInCreative True if the item should show up in creative, false otherwise
 */
abstract class AOTDItemDoor(private val door: AOTDDoor, displayInCreative: Boolean = true) : ItemDoor(door) {
    init {
        this.setRegistryName(door.registryName)

        if (displayInCreative) {
            this.creativeTab = Constants.AOTD_CREATIVE_TAB
        }
    }

    /**
     * @return the unlocalized name of this item
     */
    override fun getUnlocalizedName(): String {
        return door.unlocalizedName
    }

    /**
     * @return the unlocalized name of this item that is metadata dependant
     */
    override fun getUnlocalizedName(stack: ItemStack): String {
        return door.unlocalizedName
    }
}