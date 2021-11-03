package com.davidm1a2.afraidofthedark.common.world.loottable

import com.davidm1a2.afraidofthedark.common.constants.ModLootConditions
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.minecraft.loot.ILootSerializer
import net.minecraft.loot.LootConditionType
import net.minecraft.loot.LootContext
import net.minecraft.loot.LootParameter
import net.minecraft.loot.LootParameters
import net.minecraft.loot.conditions.ILootCondition
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.JSONUtils

class BlockEntityTag(
    private val key: String,
    private val value: String?,
    private val operator: Operator
) : ILootCondition {
    override fun getType(): LootConditionType {
        return ModLootConditions.BLOCK_ENTITY_TAG
    }

    override fun getReferencedContextParams(): MutableSet<LootParameter<*>> {
        return mutableSetOf(LootParameters.BLOCK_ENTITY)
    }

    override fun test(lootContext: LootContext): Boolean {
        val nbt = lootContext.getParamOrNull(LootParameters.BLOCK_ENTITY)?.serializeNBT() ?: return false

        return when (operator) {
            Operator.KEY_EXISTS -> nbt.contains(key)
            Operator.EQUALS -> {
                val lowestNbtTag = extractLowestNbtTag(nbt) ?: return false
                return value == lowestNbtTag.asString
            }
        }
    }

    private fun extractLowestNbtTag(nbt: CompoundNBT): INBT? {
        val subKeys = key.split(".")
        var lowestNbtTag: INBT = nbt
        for (subKey in subKeys) {
            if (lowestNbtTag !is CompoundNBT) {
                return null
            }
            lowestNbtTag = lowestNbtTag.get(subKey) ?: return null
        }
        return lowestNbtTag
    }

    internal class Serializer : ILootSerializer<BlockEntityTag> {
        override fun serialize(jsonObject: JsonObject, blockEntityTag: BlockEntityTag, context: JsonSerializationContext) {
            jsonObject.addProperty("key", blockEntityTag.key)
            blockEntityTag.value?.let { jsonObject.addProperty("value", it) }
            jsonObject.addProperty("operator", blockEntityTag.operator.name)
        }

        override fun deserialize(jsonObject: JsonObject, context: JsonDeserializationContext): BlockEntityTag {
            val key = JSONUtils.getAsString(jsonObject, "key")
            val value = if (jsonObject.has("value")) {
                JSONUtils.getAsString(jsonObject, "value")
            } else {
                null
            }
            val operator = Operator.valueOf(JSONUtils.getAsString(jsonObject, "operator"))

            return BlockEntityTag(key, value, operator)
        }
    }

    enum class Operator {
        KEY_EXISTS,
        EQUALS
    }
}