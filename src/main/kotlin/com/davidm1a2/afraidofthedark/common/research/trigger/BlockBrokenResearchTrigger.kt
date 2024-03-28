package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.world.BlockEvent
import kotlin.reflect.KClass

class BlockBrokenResearchTrigger : ResearchTrigger<BlockEvent.BreakEvent, BlockBrokenResearchTriggerConfig>(BlockBrokenResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "block_broken")
    }

    override fun getEventType(config: BlockBrokenResearchTriggerConfig): KClass<BlockEvent.BreakEvent> {
        return BlockEvent.BreakEvent::class
    }

    override fun getAffectedPlayer(event: BlockEvent.BreakEvent, config: BlockBrokenResearchTriggerConfig): Player? {
        return event.player
    }

    override fun shouldUnlock(player: Player, event: BlockEvent.BreakEvent, config: BlockBrokenResearchTriggerConfig): Boolean {
        val stateMatches = config.blockOrState.map({ event.state == it }, { event.state.block == it })
        return stateMatches && (event.state.canHarvestBlock(event.world, event.pos, player) || !config.mustDrop)
    }
}