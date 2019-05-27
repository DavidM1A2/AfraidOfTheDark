package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellPowerSources;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceEntry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register spell power source event and registers all of our spell power sources
 */
public class SpellPowerSourceRegister
{
    /**
     * Called by forge to register any of our spell power sources
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    public void registerSpellPowerSources(RegistryEvent.Register<SpellPowerSourceEntry> event)
    {
        IForgeRegistry<SpellPowerSourceEntry> registry = event.getRegistry();
        // Register all spell power sources in our mod
        registry.registerAll(ModSpellPowerSources.SPELL_POWER_SOURCES);
    }
}
