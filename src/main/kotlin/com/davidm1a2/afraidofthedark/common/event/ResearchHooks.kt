package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.item.core.AOTDArmorItem
import net.minecraft.entity.MobEntity
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import java.util.function.Consumer
import kotlin.reflect.KClass

/**
 * Contains logic for handling research unlocks.
 */
class ResearchHooks {
    private val hooks = mutableMapOf<KClass<out Event>, MutableList<Consumer<Event>>>()

    @SubscribeEvent
    fun onEvent(event: Event) {
        if (hooks[event::class] != null) {
            for (listener in hooks[event::class]!!) {
                listener.accept(event)
            }
        }
    }

    fun addHook(eventType: KClass<out Event>, consumer: Consumer<Event>) {
        if (hooks[eventType] == null) {
            hooks[eventType] = mutableListOf()
        }
        hooks[eventType]?.add(consumer)
    }
}