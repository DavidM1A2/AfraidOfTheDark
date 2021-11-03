package com.davidm1a2.afraidofthedark.common.world.loottable

import net.minecraft.loot.ILootSerializer
import net.minecraft.loot.LootConditionType
import net.minecraft.loot.conditions.ILootCondition
import net.minecraft.util.ResourceLocation

abstract class AOTDLootConditionType(serializer: ILootSerializer<out ILootCondition>, val registryName: ResourceLocation) : LootConditionType(serializer)