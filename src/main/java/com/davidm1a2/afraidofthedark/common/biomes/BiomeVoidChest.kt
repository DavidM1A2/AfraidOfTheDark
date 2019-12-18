package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeDecorator
import java.util.*

/**
 * Void chest biome is used in the void chest dimension.
 *
 * @constructor initializes the biome's fields
 * @property spawnableCreatures This list should always be empty
 */
class BiomeVoidChest : Biome(
    BiomeProperties("Void Chest")
        .setWaterColor(0x537B09)
        .setBaseHeight(0.125f)
        .setHeightVariation(0.05f)
        .setRainDisabled()
)
{
    private val spawnableCreatures: MutableList<SpawnListEntry> = ArrayList()

    init
    {
        // Set this biome's properties. It takes height, variation, water color, and a name
        // Set the biome's registry name
        registryName = ResourceLocation(Constants.MOD_ID, "void_chest")
        // The biome is blank, so nothing generates in it
        flowers.clear()
        topBlock = Blocks.AIR.defaultState
        modSpawnableLists.values.forEach { it.clear() }
    }

    /**
     * No creatures can spawn here, ever
     *
     * @param creatureType ignored
     * @return An empty list of spawnables
     */
    override fun getSpawnableList(creatureType: EnumCreatureType): List<SpawnListEntry>
    {
        // Ensure the list stays empty
        spawnableCreatures.clear()
        return spawnableCreatures
    }

    /**
     * @return 0% chance
     */
    override fun getSpawningChance(): Float
    {
        return 0f
    }

    /**
     * @return Creates an empty biome decorator
     */
    override fun createBiomeDecorator(): BiomeDecorator
    {
        // Return a biome decorator that does nothing
        return object : BiomeDecorator()
        {
            /**
             * Do nothing
             *
             * @param worldIn ignored
             * @param random ignored
             * @param biome ignored
             * @param pos ignored
             */
            override fun decorate(worldIn: World, random: Random, biome: Biome, pos: BlockPos)
            {
            }
        }
    }
}