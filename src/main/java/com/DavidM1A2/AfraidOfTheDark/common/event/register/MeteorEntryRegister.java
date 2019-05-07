package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModMeteorEntries;
import com.DavidM1A2.afraidofthedark.common.registry.meteor.MeteorEntry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register meteor entry event and registers all of our meteor entries
 */
public class MeteorEntryRegister
{
    /**
     * Called by forge to register any of our meteor entries
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    public void registerMeteorEntries(RegistryEvent.Register<MeteorEntry> event)
    {
        // Grab the registry for meteor entries
        IForgeRegistry<MeteorEntry> registry = event.getRegistry();

        // Register all of our mod meteor entries
        registry.registerAll(ModMeteorEntries.METEOR_ENTRY_LIST);
    }
}
