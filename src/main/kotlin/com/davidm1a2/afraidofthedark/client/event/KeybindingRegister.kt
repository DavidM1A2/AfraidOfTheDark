package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fmlclient.registry.ClientRegistry

class KeybindingRegister {
    @SubscribeEvent
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        ModKeybindings.KEY_BINDING_LIST.forEach { ClientRegistry.registerKeyBinding(it) }
    }
}