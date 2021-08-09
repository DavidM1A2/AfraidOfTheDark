package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModFeatures
import net.minecraft.world.gen.feature.Feature
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class used to register our features into the game
 */
class FeatureRegister {
    /**
     * Called by forge to register any of our items
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Feature<*>>) {
        val registry = event.registry

        registry.registerAll(*ModFeatures.FEATURES)
    }
}