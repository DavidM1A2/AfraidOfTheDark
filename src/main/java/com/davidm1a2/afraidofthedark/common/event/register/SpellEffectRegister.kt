package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class that receives the register effect method event and registers all of our spell effects
 */
class SpellEffectRegister {
    /**
     * Called by forge to register any of our spell effects
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerSpellEffects(event: RegistryEvent.Register<SpellEffect>) {
        val registry = event.registry

        // Register all spell effects in our mod
        registry.registerAll(*ModSpellEffects.SPELL_EFFECTS)
    }
}