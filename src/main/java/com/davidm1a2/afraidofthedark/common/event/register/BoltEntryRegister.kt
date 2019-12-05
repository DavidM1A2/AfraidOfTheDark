package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBoltEntries
import com.davidm1a2.afraidofthedark.common.registry.bolt.BoltEntry
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class that receives the register bolt entry event and registers all of our bolt entries
 */
class BoltEntryRegister
{
    /**
     * Called by forge to register any of our bolt entries
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerBoltEntries(event: RegistryEvent.Register<BoltEntry>)
    {
        // Grab the registry for bolt entries
        val registry = event.registry
        // Register all of our mod bolt entries
        registry.registerAll(*ModBoltEntries.BOLT_ENTRY_LIST)
    }
}