package com.davidm1a2.afraidofthedark.common.world.structure.gnomishcity

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class GnomishCityConfig : IFeatureConfig {
    override fun <T : Any?> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.emptyMap())
    }

    companion object {
        fun deserialize(): GnomishCityConfig {
            return GnomishCityConfig()
        }
    }
}