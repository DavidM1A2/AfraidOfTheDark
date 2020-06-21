package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModServerConfiguration
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeManager
import net.minecraftforge.common.BiomeManager.BiomeEntry
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class used to register biomes
 */
class BiomeRegister {
    /**
     * Called by forge to register any of our biomes
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerBiomes(event: RegistryEvent.Register<Biome>) {
        // Register the forest biome to our event
        event.registry.registerAll(*ModBiomes.BIOME_LIST)

        // Make sure the eerie forest can generate in warm and cool biomes
        BiomeManager.addBiome(
            BiomeManager.BiomeType.COOL,
            BiomeEntry(ModBiomes.EERIE_FOREST, ModServerConfiguration.eerieBiomeFrequency)
        )
        BiomeManager.addBiome(
            BiomeManager.BiomeType.WARM,
            BiomeEntry(ModBiomes.EERIE_FOREST, ModServerConfiguration.eerieBiomeFrequency)
        )
        // Make sure the eerie forest is registered as being compatible with forests, coniferous, and plains types
        BiomeDictionary.addTypes(
            ModBiomes.EERIE_FOREST,
            BiomeDictionary.Type.FOREST,
            BiomeDictionary.Type.CONIFEROUS,
            BiomeDictionary.Type.PLAINS
        )

        // VOID_CHEST and NIGHTMARE are set to void types
        BiomeDictionary.addTypes(ModBiomes.VOID_CHEST, BiomeDictionary.Type.VOID)
        BiomeDictionary.addTypes(ModBiomes.NIGHTMARE, BiomeDictionary.Type.VOID)
    }
}