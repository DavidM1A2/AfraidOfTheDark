package com.davidm1a2.afraidofthedark.common.world.structure.observatory

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class ObservatoryConfig(val frequency: Double) : IFeatureConfig {
    override fun <T : Any> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(mapOf(ops.createString("frequency") to ops.createDouble(frequency))))
    }

    companion object {
        fun <T> deserialize(dynamic: Dynamic<T>): ObservatoryConfig {
            return ObservatoryConfig(dynamic.get("frequency").asDouble(0.0))
        }
    }
}