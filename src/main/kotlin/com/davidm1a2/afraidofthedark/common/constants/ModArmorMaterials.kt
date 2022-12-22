package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.utility.AOTDArmorMaterial
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.SoundEvents

/**
 * Class storing armor material constants. These are registered through the enum helper class and not through the standard forge system
 */
object ModArmorMaterials {
    val ASTRAL_SILVER = AOTDArmorMaterial(
        "astral_silver",
        20,
        arrayOf(2, 5, 6, 2),
        20,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        0f,
        0f,
        Ingredient.of(ModItems.ASTRAL_SILVER_INGOT)
    )
    val IGNEOUS = AOTDArmorMaterial(
        "igneous",
        0,
        arrayOf(3, 6, 8, 3),
        20,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        0f,
        0f,
        Ingredient.of(ModItems.IGNEOUS_GEM)
    )
    val STAR_METAL = AOTDArmorMaterial(
        "star_metal",
        0,
        arrayOf(3, 6, 8, 3),
        25,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        0f,
        0f,
        Ingredient.of(ModItems.STAR_METAL_PLATE)
    )
    val ELDRITCH_METAL = AOTDArmorMaterial(
        "eldritch_metal",
        15,
        arrayOf(2, 5, 6, 2),
        9,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        0f,
        0f,
        Ingredient.of(ModItems.ELDRITCH_METAL_INGOT)
    )
    val AMORPHOUS_METAL = AOTDArmorMaterial(
        "amorphous_metal",
        40,
        arrayOf(3, 6, 8, 3),
        22,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        0f,
        0f,
        Ingredient.EMPTY
    )
    val VOID = AOTDArmorMaterial(
        "void",
        1,
        arrayOf(3, 6, 8, 3),
        75,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        0f,
        1f,
        Ingredient.EMPTY
    )
}