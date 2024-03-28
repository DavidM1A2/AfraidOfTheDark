package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Function

class UseBlockResearchTriggerConfig(val blockOrState: Either<BlockState, Block>) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<UseBlockResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                Codec.either(BlockState.CODEC, ForgeRegistries.BLOCKS.codec())
                    .fieldOf("block_or_state")
                    .forGetter(UseBlockResearchTriggerConfig::blockOrState),
            ).apply(it, it.stable(Function { blockOrState ->
                UseBlockResearchTriggerConfig(blockOrState)
            }))
        }
    }
}