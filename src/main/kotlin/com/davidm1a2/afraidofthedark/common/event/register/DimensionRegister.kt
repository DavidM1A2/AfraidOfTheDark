package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraftforge.common.ModDimension
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.world.RegisterDimensionsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that registers all AOTD dimensions into the game
 */
class DimensionRegister {
    /**
     * Called by forge to register any of our mod dimension references
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerModDimensions(event: RegistryEvent.Register<ModDimension>) {
        event.registry.registerAll(*ModDimensions.DIMENSION_LIST)
    }

    /**
     * Called by forge to register any of our mod dimensions. This is required since dimensions
     * are are created in 2 parts
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun registerDimensions(event: RegisterDimensionsEvent) {
        // There seems to be a bug in 1.14 where this does not get called client side. To avoid this issue, we lazily init
        // these variables so the client inits them whenever it wants. The server inits them here. TODO: Fix in 1.15
        ModDimensions.NIGHTMARE_TYPE
        ModDimensions.VOID_CHEST_TYPE
    }
}