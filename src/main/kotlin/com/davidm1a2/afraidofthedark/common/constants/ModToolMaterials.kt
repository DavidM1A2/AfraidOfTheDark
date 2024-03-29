package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.utility.AOTDToolMaterial
import net.minecraft.item.crafting.Ingredient

/**
 * Class storing tool material constants. These are registered through the enum helper class and not through the standard forge system
 */
object ModToolMaterials {
    val BONE = AOTDToolMaterial(0, 150, 0f, 0f, 0, Ingredient.of(ModItems.ENCHANTED_SKELETON_BONE))
    val ASTRAL_SILVER = AOTDToolMaterial(2, 280, 8f, 3.0f, 20, Ingredient.of(ModItems.ASTRAL_SILVER_INGOT))
    val IGNEOUS = AOTDToolMaterial(3, 600, 16f, 8f, 15, Ingredient.EMPTY)
    val STAR_METAL = AOTDToolMaterial(3, 600, 20f, 7f, 15, Ingredient.EMPTY)
    val GNOMISH_METAL = AOTDToolMaterial(3, 350, 20f, 0f, 10, Ingredient.of(ModItems.GNOMISH_METAL_INGOT))
    val ELDRITCH_METAL = AOTDToolMaterial(2, 280, 5.5f, 6f, 9, Ingredient.of(ModItems.ELDRITCH_METAL_INGOT))
    val AMORPHOUS_METAL = AOTDToolMaterial(3, 2000, 18f, 7f, 15, Ingredient.EMPTY)
    val VOID = AOTDToolMaterial(3, 20, 100f, 35f, 75, Ingredient.EMPTY)
}
