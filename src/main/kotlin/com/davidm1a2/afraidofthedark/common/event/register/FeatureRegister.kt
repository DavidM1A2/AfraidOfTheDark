package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModFeatures
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.Feature
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.ForgeRegistries

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

        ModFeatures.STRUCTURE_PIECES.forEach {
            Registry.register(Registry.STRUCTURE_PIECE, it.first.toString(), it.second)
        }

        registry.registerAll(*ModFeatures.FEATURES)

        ForgeRegistries.BIOMES.forEach {
            ModFeatures.FEATURES.filterIsInstance<AOTDStructure<*>>().forEach { structure -> structure.setupStructureIn(it) }
        }
    }
}