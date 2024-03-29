package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ConfiguredResearchTrigger
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.logging.log4j.LogManager
import java.time.ZonedDateTime
import kotlin.reflect.KClass

/**
 * Contains logic for handling research unlocks.
 */
class ResearchTriggerHandler {
    private val hooks = mutableMapOf<KClass<out Event>, MutableList<Pair<ConfiguredResearchTrigger<in Event, *, *>, Research>>>()

    @SubscribeEvent
    fun onEvent(event: Event) {
        hooks[event::class]?.forEach { (trigger, research) ->
            val player = trigger.getAffectedPlayer(event)
            // Only process hooks server side, otherwise we risk processing each event twice in singleplayer.
            if (player != null && !player.level.isClientSide) {
                val playerResearch = player.getResearch()
                if (playerResearch.canResearch(research)) {
                    if (trigger.shouldUnlock(player, event)) {
                        playerResearch.setResearch(research, ZonedDateTime.now(Constants.DEFAULT_TIME_ZONE))
                        playerResearch.sync(player, true)
                    }
                }
            }
        }
    }

    fun loadHooksFromResearches() {
        ModRegistries.RESEARCH.forEach { research ->
            try {
                research.triggers.forEach {
                    val hookList = hooks.computeIfAbsent(it.getEventType()) { mutableListOf() }
                    @Suppress("UNCHECKED_CAST")
                    hookList.add(it as ConfiguredResearchTrigger<in Event, *, *> to research)
                }
            } catch (e: Exception) {
                LOG.error("Failed to load research ${research.registryName.toString()}", e)
            }
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}