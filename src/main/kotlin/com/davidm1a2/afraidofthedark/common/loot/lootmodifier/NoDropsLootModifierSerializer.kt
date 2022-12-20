package com.davidm1a2.afraidofthedark.common.loot.lootmodifier

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.google.gson.JsonObject
import net.minecraft.loot.conditions.ILootCondition
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.loot.GlobalLootModifierSerializer

class NoDropsLootModifierSerializer : GlobalLootModifierSerializer<NoDropsLootModifier>() {
    init {
        setRegistryName(Constants.MOD_ID, "no_drops")
    }

    override fun read(location: ResourceLocation, obj: JsonObject, conditions: Array<out ILootCondition>): NoDropsLootModifier {
        return NoDropsLootModifier(conditions)
    }

    override fun write(instance: NoDropsLootModifier): JsonObject {
        return makeConditions(instance.conditions)
    }
}