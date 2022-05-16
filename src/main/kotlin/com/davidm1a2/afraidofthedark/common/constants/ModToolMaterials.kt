package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.utility.AOTDToolMaterial
import net.minecraft.item.crafting.Ingredient

/**
 * Class storing tool material constants. These are registered through the enum helper class and not through the standard forge system
 */
object ModToolMaterials {
    val BONE = AOTDToolMaterial(0, 150, 0f, 0f, 0, Ingredient.of(ModItems.ENCHANTED_SKELETON_BONE))
    val ASTRAL_SILVER = AOTDToolMaterial(3, 250, 8f, 3.0f, 20, Ingredient.of(ModItems.ASTRAL_SILVER_INGOT))
    val IGNEOUS = AOTDToolMaterial(3, 600, 16f, 8f, 15, Ingredient.EMPTY)
    val STAR_METAL = AOTDToolMaterial(3, 600, 20f, 7f, 15, Ingredient.EMPTY)
}
