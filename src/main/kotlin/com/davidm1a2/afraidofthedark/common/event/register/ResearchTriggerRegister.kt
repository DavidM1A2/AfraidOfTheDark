package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModResearchTriggers
import com.davidm1a2.afraidofthedark.common.event.ResearchTriggerHandler
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

class ResearchTriggerRegister(private val researchTriggerHandler: ResearchTriggerHandler) {
    @SubscribeEvent
    fun registerResearchTriggers(event: RegistryEvent.Register<ResearchTrigger<*, *>>) {
        val registry = event.registry

        // Register each research in our research list
        registry.registerAll(*ModResearchTriggers.LIST)
    }

    @SubscribeEvent
    fun registerTriggersForAllResearches(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            researchTriggerHandler.loadHooksFromResearches()
        }
    }
}