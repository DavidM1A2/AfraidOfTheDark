package com.davidm1a2.afraidofthedark.common.loot.loottable

import com.davidm1a2.afraidofthedark.common.constants.ModLootConditions
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.util.GsonHelper
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.parameters.LootContextParam
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class BlockEntityTag(
    private val key: String,
    private val value: String?,
    private val operator: Operator
) : LootItemCondition {
    override fun getType(): LootItemConditionType {
        return ModLootConditions.BLOCK_ENTITY_TAG
    }

    override fun getReferencedContextParams(): MutableSet<LootContextParam<*>> {
        return mutableSetOf(LootContextParams.BLOCK_ENTITY)
    }

    override fun test(lootContext: LootContext): Boolean {
        val nbt = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY)?.serializeNBT() ?: return false

        return when (operator) {
            Operator.KEY_EXISTS -> nbt.contains(key)
            Operator.EQUALS -> {
                val lowestNbtTag = extractLowestNbtTag(nbt) ?: return false
                return value == lowestNbtTag.asString
            }
        }
    }

    private fun extractLowestNbtTag(nbt: CompoundTag): Tag? {
        val subKeys = key.split(".")
        var lowestNbtTag: Tag = nbt
        for (subKey in subKeys) {
            if (lowestNbtTag !is CompoundTag) {
                return null
            }
            lowestNbtTag = lowestNbtTag.get(subKey) ?: return null
        }
        return lowestNbtTag
    }

    internal class Serializer : net.minecraft.world.level.storage.loot.Serializer<BlockEntityTag> {
        override fun serialize(jsonObject: JsonObject, blockEntityTag: BlockEntityTag, context: JsonSerializationContext) {
            jsonObject.addProperty("key", blockEntityTag.key)
            blockEntityTag.value?.let { jsonObject.addProperty("value", it) }
            jsonObject.addProperty("operator", blockEntityTag.operator.name)
        }

        override fun deserialize(jsonObject: JsonObject, context: JsonDeserializationContext): BlockEntityTag {
            val key = GsonHelper.getAsString(jsonObject, "key")
            val value = if (jsonObject.has("value")) {
                GsonHelper.getAsString(jsonObject, "value")
            } else {
                null
            }
            val operator = Operator.valueOf(GsonHelper.getAsString(jsonObject, "operator"))

            return BlockEntityTag(key, value, operator)
        }
    }

    enum class Operator {
        KEY_EXISTS,
        EQUALS
    }
}