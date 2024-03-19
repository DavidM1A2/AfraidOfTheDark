package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import java.util.function.Function

class BooleanConfig(val supported: Boolean) : FeatureConfiguration {
    companion object {
        val CODEC: Codec<BooleanConfig> = RecordCodecBuilder.create {
            it.group(Codec.BOOL.fieldOf("supported").orElse(false).forGetter(BooleanConfig::supported))
                .apply(it, it.stable(Function { a -> BooleanConfig(a) }))
        }
    }
}