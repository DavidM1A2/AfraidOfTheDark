package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.biomes.extras.AOTDWorldGenBigTree
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockLog
import net.minecraft.block.BlockLog.EnumAxis
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.WorldGenAbstractTree
import net.minecraft.world.gen.feature.WorldGenTrees
import java.awt.Color
import java.util.*

/**
 * Class representing the Erie Forest biome
 *
 * @constructor initializes the biome's fields
 */
class BiomeErieForest : Biome(BiomeProperties("Erie Forest")
        .setWaterColor(0x000099)
        .setBaseHeight(0.125f)
        .setHeightVariation(0.05f))
{
    init
    {
        // Set this biome's properties. It takes height, variation, water color, and a name
        decorator.grassPerChunk = 10
        // Set the biome's registry name to erie forest
        registryName = ResourceLocation(Constants.MOD_ID, "erie_forest")
        // Use stone as the underground filler block
        fillerBlock = Blocks.STONE.defaultState
        // We will have no flowers in this biome
        flowers.clear()
        // We have lots of trees per chunk
        decorator.treesPerChunk = 10
        decorator.flowersPerChunk = 0
        decorator.mushroomsPerChunk = 0
        // The top block of the biome is dirt
        topBlock = Blocks.GRASS.defaultState
    }

    /**
     * Use a brown grass color
     *
     * @param original The original grass color
     * @return The new grass color
     */
    override fun getModdedBiomeGrassColor(original: Int): Int
    {
        // hash code converts from color object to 32-bit integer, then get rid of the alpha parameter
        return Color(83, 56, 6).hashCode()
    }

    /**
     * Getter for the tree generator
     *
     * @param rand Random object to be used by the generator
     * @return The biome tree generator
     */
    override fun getRandomTreeFeature(rand: Random): WorldGenAbstractTree
    {
        // Every 3 trees is a big tree
        return if (rand.nextInt(3) == 0)
        {
            AOTDWorldGenBigTree(true,
                    ModBlocks.GRAVEWOOD.defaultState.withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
                    ModBlocks.GRAVEWOOD_LEAVES.defaultState).apply {
                leafIntegrity = 0.9
                trunkSize = 2
            }
        }
        else
        {
            WorldGenTrees(true,
                    6,
                    ModBlocks.GRAVEWOOD.defaultState.withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
                    ModBlocks.GRAVEWOOD_LEAVES.defaultState,
                    false)
        }
    }
}