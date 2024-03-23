package com.davidm1a2.afraidofthedark.common.loot.lootmodifier

import net.minecraft.client.Minecraft
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraftforge.common.loot.LootModifier
import net.minecraftforge.items.ItemHandlerHelper

class AutosmeltLootModifier(internal val conditions: Array<out LootItemCondition>) : LootModifier(conditions) {
    override fun doApply(generatedLoot: MutableList<ItemStack>, context: LootContext): MutableList<ItemStack> {
        return generatedLoot.map { smelt(it, context) }.toMutableList()
    }

    private fun smelt(stack: ItemStack, context: LootContext): ItemStack {
        val level = context.level
        return level.recipeManager.getRecipeFor(RecipeType.SMELTING, SimpleContainer(stack), level)
            .map(SmeltingRecipe::getResultItem)
            .filter { !it.isEmpty }
            .map { ItemHandlerHelper.copyStackWithSize(it, stack.count * it.count) }
            .orElse(stack)
    }
}