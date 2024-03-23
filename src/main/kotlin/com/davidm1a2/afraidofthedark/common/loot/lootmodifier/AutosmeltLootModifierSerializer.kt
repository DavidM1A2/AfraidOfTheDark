package com.davidm1a2.afraidofthedark.common.loot.lootmodifier

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.google.gson.JsonObject
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraftforge.common.loot.GlobalLootModifierSerializer

class AutosmeltLootModifierSerializer : GlobalLootModifierSerializer<AutosmeltLootModifier>() {
    init {
        setRegistryName(Constants.MOD_ID, "autosmelt")
    }

    override fun read(location: ResourceLocation, obj: JsonObject, conditions: Array<out LootItemCondition>): AutosmeltLootModifier {
        return AutosmeltLootModifier(conditions)
    }

    override fun write(instance: AutosmeltLootModifier): JsonObject {
        return makeConditions(instance.conditions)
    }
}