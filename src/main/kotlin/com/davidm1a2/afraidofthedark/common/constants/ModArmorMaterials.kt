package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.utility.AOTDArmorMaterial
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.SoundEvents

/**
 * Class storing armor material constants. These are registered through the enum helper class and not through the standard forge system
 */
object ModArmorMaterials {
    val IGNEOUS = AOTDArmorMaterial(
        "igneous",
        0,
        arrayOf(0, 0, 0, 0),
        20,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        0f,
        0f,
        Ingredient.of(ModItems.IGNEOUS_GEM)
    )
    val STAR_METAL = AOTDArmorMaterial(
        "star_metal",
        0,
        arrayOf(0, 0, 0, 0),
        20,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        0f,
        0f,
        Ingredient.of(ModItems.STAR_METAL_PLATE)
    )
}