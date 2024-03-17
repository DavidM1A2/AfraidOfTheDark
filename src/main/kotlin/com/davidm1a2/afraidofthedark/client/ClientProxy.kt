package com.davidm1a2.afraidofthedark.client

import com.davidm1a2.afraidofthedark.client.event.*
import com.davidm1a2.afraidofthedark.common.IProxy
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.eventbus.api.IEventBus

class ClientProxy : IProxy {
    private val minecraft = Minecraft.getInstance()
    override val researchOverlayHandler: ResearchOverlayHandler = ClientResearchOverlayHandler()

    override fun registerSidedHandlers(forgeBus: IEventBus, modBus: IEventBus) {
        forgeBus.register(researchOverlayHandler)
        forgeBus.register(ClientNightmareHandler())
        forgeBus.register(ClientSpellFreezeHandler())
        forgeBus.register(ClientLateRenderHandler())

        modBus.register(ModColorRegister())
        modBus.register(BlockRenderTypeRegister())
        modBus.register(EntityRendererRegister())
        modBus.register(BlockEntityRendererRegister())
        modBus.register(DimensionRenderInfoRegister())
        modBus.register(ItemModelPropertyRegister())
        modBus.register(KeybindingRegister())
        modBus.register(ResearchPositionHandler())
        modBus.register(TextureAtlasRegister())
    }

    override fun playSoundFixed(entity: Entity, event: SoundEvent, category: SoundSource, volume: Float, pitch: Float) {
        val eventResult = ForgeEventFactory.onPlaySoundAtEntity(entity, event, category, volume, pitch)
        if (eventResult.isCanceled || eventResult.sound == null) return

        this.minecraft.soundManager.play(EntityBoundSoundInstance(eventResult.sound, eventResult.category, eventResult.volume, eventResult.pitch, eventResult.entity))
    }
}