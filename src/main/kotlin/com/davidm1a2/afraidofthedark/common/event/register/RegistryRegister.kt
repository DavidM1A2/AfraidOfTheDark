package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.BoltEntry
import com.davidm1a2.afraidofthedark.common.registry.MeteorEntry
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent.NewRegistry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.RegistryBuilder

/**
 * Class used to register any custom registries we are using
 */
class RegistryRegister {
    /**
     * Called to add any custom new registries to the game. This must be static in order for the event
     * bus subscriber annotation to fire and add our custom registry
     *
     * @param event The event specifying we can now add new registries into the game
     */
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun registryRegister(event: NewRegistry) {
        // Create a new registry for research with the name 'afraidofthedark:research'
        ModRegistries.RESEARCH = RegistryBuilder<Research>()
            .setType(Research::class.java)
            .setName(ResourceLocation(Constants.MOD_ID, "research"))
            .create()

        // Create a new registry for the different bolt types with the name 'afraidofthedark:bolts'
        ModRegistries.BOLTS = RegistryBuilder<BoltEntry>()
            .setType(BoltEntry::class.java)
            .setName(ResourceLocation(Constants.MOD_ID, "bolts"))
            .create()

        // Create a new registry for the different meteor types with the name 'afraidofthedark:meteors'
        ModRegistries.METEORS = RegistryBuilder<MeteorEntry>()
            .setType(MeteorEntry::class.java)
            .setName(ResourceLocation(Constants.MOD_ID, "meteors"))
            .create()

        // Create a new registry for the different spell power sources with the name 'afraidofthedark:spell_power_sources'
        ModRegistries.SPELL_POWER_SOURCES = RegistryBuilder<SpellPowerSource>()
            .setType(SpellPowerSource::class.java)
            .setName(ResourceLocation(Constants.MOD_ID, "spell_power_sources"))
            .create()

        // Create a new registry for the different spell delivery methods with the name 'afraidofthedark:spell_delivery_methods'
        ModRegistries.SPELL_DELIVERY_METHODS = RegistryBuilder<SpellDeliveryMethod>()
            .setType(SpellDeliveryMethod::class.java)
            .setName(ResourceLocation(Constants.MOD_ID, "spell_delivery_methods"))
            .create()

        // Create a new registry for the different spell effects with the name 'afraidofthedark:spell_effects'
        ModRegistries.SPELL_EFFECTS = RegistryBuilder<SpellEffect>()
            .setType(SpellEffect::class.java)
            .setName(ResourceLocation(Constants.MOD_ID, "spell_effects"))
            .create()

        // Create a new registry for the different research triggers with the name 'afraidofthedark:research_trigger'
        ModRegistries.RESEARCH_TRIGGERS = RegistryBuilder<ResearchTrigger<*>>()
            .setType(ResearchTrigger::class.java)
            .setName(ResourceLocation(Constants.MOD_ID, "research_triggers"))
            .create()
    }
}