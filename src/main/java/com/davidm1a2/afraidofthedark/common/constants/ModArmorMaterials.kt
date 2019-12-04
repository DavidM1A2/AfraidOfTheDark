package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper

/**
 * Class storing armor material constants. These are registered through the enum helper class and not through the standard forge system
 */
object ModArmorMaterials
{
    val IGNEOUS = EnumHelper.addArmorMaterial("igneous", "texture", 45, intArrayOf(3, 8, 6, 3), 20, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0f)
    val STAR_METAL = EnumHelper.addArmorMaterial("star_metal", "texture", 45, intArrayOf(3, 8, 6, 3), 20, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3.0f)

    // Set material repair items
    init
    {
        IGNEOUS!!.setRepairItem(ItemStack(ModItems.IGNEOUS_GEM))
        STAR_METAL!!.setRepairItem(ItemStack(ModItems.STAR_METAL_PLATE))
    }
}