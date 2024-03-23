package com.davidm1a2.afraidofthedark.common.loot.lootmodifier

import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraftforge.common.loot.LootModifier

class DoubleDropsLootModifier(internal val conditions: Array<out LootItemCondition>) : LootModifier(conditions) {
    override fun doApply(generatedLoot: MutableList<ItemStack>, context: LootContext): MutableList<ItemStack> {
        for (i in 0 until generatedLoot.size) {
            val stack = generatedLoot[i]
            // Double the stack size if it can be, otherwise just add a duplicate of the stack to the end of the list
            if (stack.count <= 32) {
                stack.count = stack.count * 2
            } else {
                generatedLoot.add(stack)
            }
        }

        return generatedLoot
    }
}