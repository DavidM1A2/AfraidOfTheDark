package com.DavidM1A2.afraidofthedark.common.biomes;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

/**
 * Biome to be used by the entire nightmare dimension. It does nothing but set colors
 */
public class BiomeNightmare extends Biome
{
    // A list of spawnable creatures, should always be empty
    private List<SpawnListEntry> spawnableCreatures = new ArrayList<>();

    /**
     * Constructor initializes the biome's fields
     */
    public BiomeNightmare()
    {
        // Set this biome's properties. It takes height, variation, water color, and a name
        super(new BiomeProperties("Nightmare")
                .setWaterColor(0xFF0000)
                .setBaseHeight(0.125f)
                .setHeightVariation(0.05f)
                .setRainfall(1.0f));
        // Set the biome's registry name to nightmare
        this.setRegistryName(new ResourceLocation(Constants.MOD_ID, "nightmare"));
        // We will have no flowers or creatures in this biome
        this.flowers.clear();
        this.modSpawnableLists.values().forEach(List::clear);
    }

    /**
     * Returns an empty list
     *
     * @param creatureType The creature type to test
     * @return An empty list
     */
    @Override
    public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType)
    {
        if (!spawnableCreatures.isEmpty())
            this.spawnableCreatures.clear();
        return spawnableCreatures;
    }

    /**
     * @return 0, nothing spawns here
     */
    @Override
    public float getSpawningChance()
    {
        return 0.0f;
    }
}
