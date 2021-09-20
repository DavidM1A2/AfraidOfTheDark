package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.BiFunction

class BlockBrokenResearchTriggerConfig(val blockOrState: Either<BlockState, Block>, val mustDrop: Boolean) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<BlockBrokenResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                Codec.either(BlockState.CODEC, ForgeRegistries.BLOCKS.codec())
                    .fieldOf("block_or_state")
                    .forGetter(BlockBrokenResearchTriggerConfig::blockOrState),
                Codec.BOOL.optionalFieldOf("must_drop", false).forGetter(BlockBrokenResearchTriggerConfig::mustDrop)
            ).apply(it, it.stable(BiFunction { blockState, mustDrop ->
                BlockBrokenResearchTriggerConfig(blockState, mustDrop)
            }))
        }
    }
}