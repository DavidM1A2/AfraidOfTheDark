package com.davidm1a2.afraidofthedark.common.crafting.ingredient

import com.google.gson.JsonObject
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.network.PacketBuffer
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.common.crafting.IIngredientSerializer
import net.minecraftforge.common.crafting.IngredientNBT

class FixedForgeNbtIngredient(private val stack: ItemStack) : IngredientNBT(stack) {
    override fun getSerializer(): IIngredientSerializer<out Ingredient> {
        return Serializer
    }

    internal object Serializer : IIngredientSerializer<FixedForgeNbtIngredient> {
        override fun parse(buffer: PacketBuffer): FixedForgeNbtIngredient {
            return FixedForgeNbtIngredient(buffer.readItemStack())
        }

        override fun parse(json: JsonObject): FixedForgeNbtIngredient {
            return FixedForgeNbtIngredient(CraftingHelper.getItemStack(json, true))
        }

        override fun write(buffer: PacketBuffer, ingredient: FixedForgeNbtIngredient) {
            buffer.writeItemStack(ingredient.stack)
        }
    }
}