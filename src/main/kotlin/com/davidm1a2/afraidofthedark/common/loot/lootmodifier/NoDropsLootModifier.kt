package com.davidm1a2.afraidofthedark.common.loot.lootmodifier

import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraftforge.common.loot.LootModifier

class NoDropsLootModifier(internal val conditions: Array<out LootItemCondition>) : LootModifier(conditions) {
    override fun doApply(generatedLoot: MutableList<ItemStack>, context: LootContext): MutableList<ItemStack> {
        return mutableListOf()
    }
}