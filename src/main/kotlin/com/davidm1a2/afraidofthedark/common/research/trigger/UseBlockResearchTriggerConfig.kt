package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import java.util.function.Function

class UseBlockResearchTriggerConfig(
    val blockState: BlockState
) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<UseBlockResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                BlockState.CODEC.fieldOf("block_state").forGetter(UseBlockResearchTriggerConfig::blockState)
            ).apply(it, it.stable(Function { blockState ->
                UseBlockResearchTriggerConfig(blockState)
            }))
        }
    }
}