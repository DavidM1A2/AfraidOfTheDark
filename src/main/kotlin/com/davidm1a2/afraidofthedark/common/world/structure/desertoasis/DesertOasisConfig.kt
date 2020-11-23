package com.davidm1a2.afraidofthedark.common.world.structure.desertoasis

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class DesertOasisConfig(val supported: Boolean) : IFeatureConfig {
    override fun <T : Any> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(mapOf(ops.createString("supported") to ops.createBoolean(supported))))
    }

    companion object {
        fun <T> deserialize(dynamic: Dynamic<T>): DesertOasisConfig {
            return DesertOasisConfig(dynamic.get("supported").asBoolean(false))
        }
    }
}