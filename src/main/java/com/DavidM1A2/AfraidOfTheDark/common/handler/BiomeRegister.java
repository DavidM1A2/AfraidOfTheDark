package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.constants.ModBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Class used to register biomes
 */
public class BiomeRegister
{
	/**
	 * Called by forge to register any of our biomes
	 *
	 * @param event The event to register to
	 */
	@SubscribeEvent
	public void registerBiomes(RegistryEvent.Register<Biome> event)
	{
		// Register the forest biome to our event
		event.getRegistry().register(ModBiomes.ERIE_FOREST);

		// Make sure the erie biome can generate in warm and cool biomes
		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(ModBiomes.ERIE_FOREST, ConfigurationHandler.getInstance().getErieBiomeFrequency()));
		BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(ModBiomes.ERIE_FOREST, ConfigurationHandler.getInstance().getErieBiomeFrequency()));
		// Make sure the erie forest is registered as being compatible with forests, coniferous, and plains types
		BiomeDictionary.addTypes(ModBiomes.ERIE_FOREST, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.PLAINS);
	}
}
