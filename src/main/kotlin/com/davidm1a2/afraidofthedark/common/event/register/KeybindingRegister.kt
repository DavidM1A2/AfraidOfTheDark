package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class KeybindingRegister {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        ModKeybindings.KEY_BINDING_LIST.forEach { ClientRegistry.registerKeyBinding(it) }
    }
}