package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ConfiguredResearchTrigger
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.reflect.KClass

/**
 * Contains logic for handling research unlocks.
 */
class ResearchTriggerHandler {
    private val hooks = mutableMapOf<KClass<out Event>, MutableList<ConfiguredResearchTrigger<in Event, *, *>>>()
    private val triggerToResearch = mutableMapOf<ConfiguredResearchTrigger<*, *, *>, Research>()

    @SubscribeEvent
    fun onEvent(event: Event) {
        hooks[event::class]?.forEach {
            val player = it.getAffectedPlayer(event)
            // Only process hooks server side, otherwise we risk processing each event twice in singleplayer.
            if (player != null && !player.level.isClientSide) {
                if (it.shouldUnlock(player, event)) {
                    val research = triggerToResearch[it]!!
                    val playerResearch = player.getResearch()
                    if (playerResearch.canResearch(research)) {
                        playerResearch.setResearch(research, true)
                        playerResearch.sync(player, true)
                    }
                }
            }
        }
    }

    fun loadHooksFromResearches() {
        ModRegistries.RESEARCH.forEach { research ->
            research.triggers.forEach {
                val hookList = hooks.computeIfAbsent(it.trigger.type) { mutableListOf() }
                @Suppress("UNCHECKED_CAST")
                hookList.add(it as ConfiguredResearchTrigger<in Event, *, *>)
                triggerToResearch[it] = research
            }
        }
    }
}