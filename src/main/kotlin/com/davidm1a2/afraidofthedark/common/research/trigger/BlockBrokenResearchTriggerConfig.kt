package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import java.util.function.BiFunction

class BlockBrokenResearchTriggerConfig(val blockState: BlockState, val mustDrop: Boolean) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<BlockBrokenResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                BlockState.CODEC.fieldOf("block_state").forGetter(BlockBrokenResearchTriggerConfig::blockState),
                Codec.BOOL.optionalFieldOf("must_drop", false).forGetter(BlockBrokenResearchTriggerConfig::mustDrop)
            ).apply(it, it.stable(BiFunction { blockState, mustDrop ->
                BlockBrokenResearchTriggerConfig(blockState, mustDrop)
            }))
        }
    }
}