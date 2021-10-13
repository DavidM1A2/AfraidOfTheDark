package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.event.world.BlockEvent
import kotlin.reflect.KClass

class BlockBrokenResearchTrigger : ResearchTrigger<BlockEvent.BreakEvent, BlockBrokenResearchTriggerConfig>(BlockBrokenResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "block_broken")
    }

    override fun getEventType(config: BlockBrokenResearchTriggerConfig): KClass<BlockEvent.BreakEvent> {
        return BlockEvent.BreakEvent::class
    }

    override fun getAffectedPlayer(event: BlockEvent.BreakEvent, config: BlockBrokenResearchTriggerConfig): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: BlockEvent.BreakEvent, config: BlockBrokenResearchTriggerConfig): Boolean {
        val stateMatches = config.blockOrState.map({ event.state == it }, { event.state.block == it })
        return stateMatches && (ForgeHooks.canHarvestBlock(event.state, player, event.world, event.pos) || !config.mustDrop)
    }
}