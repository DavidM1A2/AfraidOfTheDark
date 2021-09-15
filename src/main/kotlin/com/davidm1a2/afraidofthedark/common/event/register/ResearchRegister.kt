package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.research.ResearchWrapper
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
import kotlin.math.max

/**
 * Class that receives the register research event and registers all of our researches
 */
class ResearchRegister {
    /**
     * Called by forge to register any of our researches
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerResearch(event: RegistryEvent.Register<Research>) {
        val registry = event.registry

        // Register each research in our research list
        registry.registerAll(*ModResearches.RESEARCH_LIST)
    }
}