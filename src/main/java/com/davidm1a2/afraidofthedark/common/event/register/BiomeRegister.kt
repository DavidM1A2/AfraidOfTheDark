package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeManager
import net.minecraftforge.common.BiomeManager.BiomeEntry
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

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
        // Grab our configuration
        val configurationHandler = AfraidOfTheDark.INSTANCE.configurationHandler

        // Make sure the erie forest can generate in warm and cool biomes
        BiomeManager.addBiome(
            BiomeManager.BiomeType.COOL,
            BiomeEntry(ModBiomes.ERIE_FOREST, configurationHandler.erieBiomeFrequency)
        )
        BiomeManager.addBiome(
            BiomeManager.BiomeType.WARM,
            BiomeEntry(ModBiomes.ERIE_FOREST, configurationHandler.erieBiomeFrequency)
        )
        // Make sure the erie forest is registered as being compatible with forests, coniferous, and plains types
        BiomeDictionary.addTypes(
            ModBiomes.ERIE_FOREST,
            BiomeDictionary.Type.FOREST,
            BiomeDictionary.Type.CONIFEROUS,
            BiomeDictionary.Type.PLAINS
        )

        // VOID_CHEST and NIGHTMARE are set to void types
        BiomeDictionary.addTypes(ModBiomes.VOID_CHEST, BiomeDictionary.Type.VOID)
        BiomeDictionary.addTypes(ModBiomes.NIGHTMARE, BiomeDictionary.Type.VOID)
    }
}