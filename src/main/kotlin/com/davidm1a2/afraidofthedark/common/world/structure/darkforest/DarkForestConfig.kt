package com.davidm1a2.afraidofthedark.common.world.structure.darkforest

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class DarkForestConfig(val frequency: Double) : IFeatureConfig {
    override fun <T : Any> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(mapOf(ops.createString("frequency") to ops.createDouble(frequency))))
    }

    companion object {
        fun <T> deserialize(dynamic: Dynamic<T>): DarkForestConfig {
            return DarkForestConfig(dynamic.get("frequency").asDouble(0.0))
        }
    }
}