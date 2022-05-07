package com.davidm1a2.afraidofthedark.common

import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import net.minecraft.entity.Entity
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraftforge.eventbus.api.IEventBus

interface IProxy {
    val researchOverlayHandler: ResearchOverlayHandler

    fun registerSidedHandlers(forgeBus: IEventBus, modBus: IEventBus)

    /**
     * Minecraft's World::playSound(PlayerEntity, Entity, SoundEvent, SoundCategory, float, float) doesn't correctly pass
     * on args. *sigh* This is a fixed version of the function
     */
    fun playSoundFixed(entity: Entity, event: SoundEvent, category: SoundCategory, volume: Float, pitch: Float)
}