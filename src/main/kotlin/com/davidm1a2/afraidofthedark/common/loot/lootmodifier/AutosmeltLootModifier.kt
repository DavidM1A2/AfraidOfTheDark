package com.davidm1a2.afraidofthedark.common.loot.lootmodifier

import net.minecraft.inventory.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.loot.LootContext
import net.minecraft.loot.conditions.ILootCondition
import net.minecraftforge.common.loot.LootModifier
import net.minecraftforge.items.ItemHandlerHelper

class AutosmeltLootModifier(internal val conditions: Array<out ILootCondition>) : LootModifier(conditions) {
    override fun doApply(generatedLoot: MutableList<ItemStack>, context: LootContext): MutableList<ItemStack> {
        return generatedLoot.map { smelt(it, context) }.toMutableList()
    }

    private fun smelt(stack: ItemStack, context: LootContext): ItemStack {
        val level = context.level
        return level.recipeManager.getRecipeFor(IRecipeType.SMELTING, Inventory(stack), level)
            .map(FurnaceRecipe::getResultItem)
            .filter { !it.isEmpty }
            .map { ItemHandlerHelper.copyStackWithSize(it, stack.count * it.count) }
            .orElse(stack)
    }
}