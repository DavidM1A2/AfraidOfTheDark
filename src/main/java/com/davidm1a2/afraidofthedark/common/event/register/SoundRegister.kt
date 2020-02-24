package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.util.SoundEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class that receives the register sound event and registers all of our sounds
 */
class SoundRegister {
    /**
     * Called by forge to register any of our sounds
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerSounds(event: RegistryEvent.Register<SoundEvent>) {
        val registry = event.registry

        // Register all sounds in our mod
        registry.registerAll(*ModSounds.SOUND_LIST)
    }
}