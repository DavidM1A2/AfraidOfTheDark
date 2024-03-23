package com.davidm1a2.afraidofthedark.common.loot.loottable

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.Serializer
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

abstract class AOTDLootConditionType(serializer: Serializer<out LootItemCondition>, val registryName: ResourceLocation) : LootItemConditionType(serializer)