package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register effect method event and registers all of our spell effects
 */
public class SpellEffectRegister
{
    /**
     * Called by forge to register any of our spell effects
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    public void registerSpellEffects(RegistryEvent.Register<SpellEffectEntry> event)
    {
        IForgeRegistry<SpellEffectEntry> registry = event.getRegistry();
        // Register all spell effects in our mod
        registry.registerAll(ModSpellEffects.SPELL_EFFECTS);
    }
}
