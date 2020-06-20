package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import net.minecraft.entity.EntityType
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that receives the register entity event and registers all of our entities
 */
class EntityRegister {
    /**
     * Called by forge to register any of our entities
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerEntities(event: RegistryEvent.Register<EntityType<*>>) {
        // Grab the registry for entities
        val registry = event.registry

        // Register all of our mod entities
        registry.registerAll(*ModEntities.ENTITY_LIST)
    }
}