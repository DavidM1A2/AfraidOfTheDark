package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModMeteorEntries
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class that receives the register meteor entry event and registers all of our meteor entries
 */
class MeteorEntryRegister
{
    /**
     * Called by forge to register any of our meteor entries
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerMeteorEntries(event: RegistryEvent.Register<MeteorEntry>)
    {
        // Grab the registry for meteor entries
        val registry = event.getRegistry()
        // Register all of our mod meteor entries
        registry.registerAll(*ModMeteorEntries.METEOR_ENTRY_LIST)
    }
}