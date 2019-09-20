package com.davidm1a2.afraidofthedark.common.constants;

import com.davidm1a2.afraidofthedark.common.registry.bolt.BoltEntry;
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry;
import com.davidm1a2.afraidofthedark.common.registry.research.Research;
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceEntry;
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class containing references to any registries AOTD adds. We can't actually init the registry here though, so that is done
 * inside RegisterRegistries
 */
public class ModRegistries
{
    // Fields that are unchanged and basically final (just not initialized here) representing the registries we are adding. Initialized from RegistryRegister
    public static IForgeRegistry<Structure> STRUCTURE;
    public static IForgeRegistry<Research> RESEARCH;
    public static IForgeRegistry<BoltEntry> BOLTS;
    public static IForgeRegistry<MeteorEntry> METEORS;
    public static IForgeRegistry<SpellPowerSourceEntry> SPELL_POWER_SOURCES;
    public static IForgeRegistry<SpellDeliveryMethodEntry> SPELL_DELIVERY_METHODS;
    public static IForgeRegistry<SpellEffectEntry> SPELL_EFFECTS;
}
