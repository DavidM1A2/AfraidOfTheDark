package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that receives the register spell power source event and registers all of our spell power sources
 */
class SpellPowerSourceRegister {
    /**
     * Called by forge to register any of our spell power sources
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerSpellPowerSources(event: RegistryEvent.Register<SpellPowerSource<*>>) {
        val registry = event.registry

        // Register all spell power sources in our mod
        registry.registerAll(*ModSpellPowerSources.SPELL_POWER_SOURCES)
    }
}