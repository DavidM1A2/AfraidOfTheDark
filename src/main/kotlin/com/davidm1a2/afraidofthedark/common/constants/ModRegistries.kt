package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.registry.BoltEntry
import com.davidm1a2.afraidofthedark.common.registry.MeteorEntry
import com.davidm1a2.afraidofthedark.common.registry.Research
import com.davidm1a2.afraidofthedark.common.researchTriggers.ConfiguredResearchTrigger
import com.davidm1a2.afraidofthedark.common.researchTriggers.ResearchTrigger
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraftforge.registries.IForgeRegistry

/**
 * Class containing references to any registries AOTD adds. We can't actually init the registry here though, so that is done
 * inside RegisterRegistries
 */
object ModRegistries {
    // Fields that are unchanged and basically final (just not initialized here) representing the registries we are adding. Initialized from RegistryRegister
    lateinit var RESEARCH: IForgeRegistry<Research>
    lateinit var BOLTS: IForgeRegistry<BoltEntry>
    lateinit var METEORS: IForgeRegistry<MeteorEntry>
    lateinit var SPELL_POWER_SOURCES: IForgeRegistry<SpellPowerSource>
    lateinit var SPELL_DELIVERY_METHODS: IForgeRegistry<SpellDeliveryMethod>
    lateinit var SPELL_EFFECTS: IForgeRegistry<SpellEffect>
    lateinit var RESEARCH_TRIGGERS: IForgeRegistry<ResearchTrigger<*>>
    lateinit var CONFIGURED_RESEARCH_TRIGGERS: IForgeRegistry<ConfiguredResearchTrigger<*, *>>
}