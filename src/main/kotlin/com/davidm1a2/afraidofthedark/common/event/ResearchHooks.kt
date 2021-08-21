package com.davidm1a2.afraidofthedark.common.event

import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.reflect.KClass

/**
 * Contains logic for handling research unlocks.
 */
class ResearchHooks {
    private val hooks = mutableMapOf<KClass<out Event>, MutableList<(Event) -> Unit>>()

    @SubscribeEvent
    fun onEvent(event: Event) {
        if (hooks[event::class] != null) {
            for (listener in hooks[event::class]!!) {
                listener(event)
            }
        }
    }

    fun addHook(eventType: KClass<out Event>, consumer: (Event) -> Unit) {
        val hook = hooks.computeIfAbsent(eventType) { mutableListOf() }
        hook.add(consumer)
    }
}