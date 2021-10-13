package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerInBiomeEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import kotlin.reflect.KClass

class InBiomeResearchTrigger : ResearchTrigger<PlayerInBiomeEvent, InBiomeResearchTriggerConfig>(InBiomeResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "in_biome")
    }

    override fun getEventType(config: InBiomeResearchTriggerConfig): KClass<PlayerInBiomeEvent> {
        return PlayerInBiomeEvent::class
    }

    override fun getAffectedPlayer(event: PlayerInBiomeEvent, config: InBiomeResearchTriggerConfig): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerInBiomeEvent, config: InBiomeResearchTriggerConfig): Boolean {
        // Unfortunately biome instances might differ even though they represent the same biome. *sigh* This is because
        // some biome instances are created by JSON and others are created in code. In the future we should probably compare
        // registry keys
        return event.biome.registryName == config.biome.registryName
    }
}