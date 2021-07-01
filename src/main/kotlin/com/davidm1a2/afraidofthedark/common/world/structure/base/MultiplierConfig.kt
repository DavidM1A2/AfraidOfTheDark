package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class MultiplierConfig(val multiplier: Int) : IFeatureConfig {
    override fun <T : Any> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(mapOf(ops.createString("multiplier") to ops.createInt(multiplier))))
    }

    companion object {
        fun <T> deserialize(dynamic: Dynamic<T>): MultiplierConfig {
            return MultiplierConfig(dynamic.get("multiplier").asInt(0))
        }
    }
}