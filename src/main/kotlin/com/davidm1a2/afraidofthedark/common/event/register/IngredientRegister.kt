package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.crafting.ingredient.FixedForgeNbtIngredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.CraftingHelper

object IngredientRegister {
    fun register() {
        CraftingHelper.register(ResourceLocation(Constants.MOD_ID, "fixed_forge_nbt"), FixedForgeNbtIngredient.Serializer)
    }
}