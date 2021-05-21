package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class BooleanConfig(val supported: Boolean) : IFeatureConfig {
    override fun <T : Any> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(mapOf(ops.createString("supported") to ops.createBoolean(supported))))
    }

    companion object {
        fun <T> deserialize(dynamic: Dynamic<T>): BooleanConfig {
            return BooleanConfig(dynamic.get("supported").asBoolean(false))
        }
    }
}