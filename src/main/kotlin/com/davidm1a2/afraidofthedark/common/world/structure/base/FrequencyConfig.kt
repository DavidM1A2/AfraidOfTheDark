package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class FrequencyConfig(val frequency: Double) : IFeatureConfig {
    override fun <T : Any> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(mapOf(ops.createString("frequency") to ops.createDouble(frequency))))
    }

    companion object {
        fun <T> deserialize(dynamic: Dynamic<T>): FrequencyConfig {
            return FrequencyConfig(dynamic.get("frequency").asDouble(0.0))
        }
    }
}