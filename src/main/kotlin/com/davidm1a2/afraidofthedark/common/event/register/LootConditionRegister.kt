package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModLootConditions
import net.minecraft.util.registry.Registry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

class LootConditionRegister {
    @SubscribeEvent
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            ModLootConditions.LIST.forEach {
                Registry.register(Registry.LOOT_CONDITION_TYPE, it.registryName, it)
            }
        }
    }
}