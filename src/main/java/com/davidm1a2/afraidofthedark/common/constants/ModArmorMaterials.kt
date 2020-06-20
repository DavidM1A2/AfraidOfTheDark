package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.utility.AOTDArmorMaterial
import net.minecraft.init.SoundEvents
import net.minecraft.item.crafting.Ingredient

/**
 * Class storing armor material constants. These are registered through the enum helper class and not through the standard forge system
 */
object ModArmorMaterials {
    val IGNEOUS = AOTDArmorMaterial(
        "igneous",
        0,
        arrayOf(0, 0, 0, 0),
        20,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        0f,
        Ingredient.fromItems(ModItems.IGNEOUS_GEM)
    )
    val STAR_METAL = AOTDArmorMaterial(
        "star_metal",
        0,
        arrayOf(0, 0, 0, 0),
        20,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        0f,
        Ingredient.fromItems(ModItems.STAR_METAL_PLATE)
    )
}