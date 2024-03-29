package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.gen.feature.IFeatureConfig
import java.util.function.Function

class MultiplierConfig(val multiplier: Int) : IFeatureConfig {
    companion object {
        val CODEC: Codec<MultiplierConfig> = RecordCodecBuilder.create {
            it.group(Codec.INT.fieldOf("multiplier").orElse(0).forGetter(MultiplierConfig::multiplier))
                .apply(it, it.stable(Function { a -> MultiplierConfig(a) }))
        }
    }
}