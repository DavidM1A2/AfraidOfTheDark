package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeAmbience
import net.minecraft.world.biome.BiomeGenerationSettings
import net.minecraft.world.biome.MobSpawnInfo
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeManager
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

class BiomeRegister {
    @SubscribeEvent
    fun registerBiomes(event: RegistryEvent.Register<Biome>) {
        // For some reason we have to register a dummy version of each overworld biome in code so MC knows about it during the create world screen. It
        // gets overwritten once the real json file is loaded during world creation. TODO: In 1.17 this shouldn't be needed
        event.registry.registerAll(*ModBiomes.LIST.map { makeDummyBiome(it.location()) }.toTypedArray())
    }

    @SubscribeEvent
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            // Make sure the eerie forest can generate in warm and cool biomes
            BiomeManager.addBiome(
                BiomeManager.BiomeType.COOL,
                BiomeManager.BiomeEntry(ModBiomes.EERIE_FOREST, ModCommonConfiguration.eerieBiomeFrequency)
            )
            BiomeManager.addBiome(
                BiomeManager.BiomeType.WARM,
                BiomeManager.BiomeEntry(ModBiomes.EERIE_FOREST, ModCommonConfiguration.eerieBiomeFrequency)
            )

            // Make sure the eerie forest is registered as being compatible with forests, coniferous, and plains types
            BiomeDictionary.addTypes(
                ModBiomes.EERIE_FOREST,
                BiomeDictionary.Type.FOREST,
                BiomeDictionary.Type.CONIFEROUS,
                BiomeDictionary.Type.PLAINS
            )
        }
    }

    private fun makeDummyBiome(registryName: ResourceLocation): Biome {
        // Fill in all required fields
        return Biome.Builder()
            .depth(0f)
            .downfall(0f)
            .temperature(0f)
            .scale(0f)
            .biomeCategory(Biome.Category.NONE)
            .generationSettings(BiomeGenerationSettings.EMPTY)
            .mobSpawnSettings(MobSpawnInfo.EMPTY)
            .precipitation(Biome.RainType.NONE)
            .specialEffects(
                BiomeAmbience.Builder()
                    .fogColor(0)
                    .waterColor(0)
                    .waterFogColor(0)
                    .skyColor(0)
                    .build()
            )
            .build().apply { setRegistryName(registryName) }
    }
}