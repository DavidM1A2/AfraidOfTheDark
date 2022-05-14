package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModLootModifierSerializers
import net.minecraftforge.common.loot.GlobalLootModifierSerializer
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class LootModifierSerializerRegister {
    @SubscribeEvent
    fun registerLootModifierSerializer(event: RegistryEvent.Register<GlobalLootModifierSerializer<*>>) {
        val registry = event.registry

        registry.registerAll(*ModLootModifierSerializers.LIST)
    }
}