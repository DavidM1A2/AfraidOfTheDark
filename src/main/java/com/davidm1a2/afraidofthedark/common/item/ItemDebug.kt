package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

/**
 * Item that allows for modding debug, does nothing else
 *
 * @constructor sets up item properties
 */
class ItemDebug : AOTDItem("debug", displayInCreative = false) {
    init {
        setMaxStackSize(1)
    }

    ///
    /// Code below here is not documented due to its temporary nature used for testing
    ///

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        if (worldIn.isRemote) {

        } else {

        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }
}