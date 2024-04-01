package com.davidm1a2.afraidofthedark.common

import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraftforge.eventbus.api.IEventBus

interface IProxy {
    fun registerSidedHandlers(forgeBus: IEventBus, modBus: IEventBus)

    /**
     * Minecraft's World::playSound(PlayerEntity, Entity, SoundEvent, SoundCategory, float, float) doesn't correctly pass
     * on args. *sigh* This is a fixed version of the function
     */
    fun playSoundFixed(entity: Entity, event: SoundEvent, category: SoundSource, volume: Float, pitch: Float)

    fun displayResearch(research: Research)
}