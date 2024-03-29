package com.davidm1a2.afraidofthedark.common.world.structure.voidchestportal

import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.util.RegistryKey
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.Random

class VoidChestPortalStructure : AOTDStructure<NoFeatureConfig>("void_chest_portal", NoFeatureConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.VOID_CHEST_PORTAL.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.VOID_CHEST_PORTAL.getLength().toInt()
    }

    override fun getStartFactory(): IStartFactory<NoFeatureConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            VoidChestPortalStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun configured(biome: RegistryKey<Biome>, category: Biome.Category): StructureFeature<NoFeatureConfig, out Structure<NoFeatureConfig>>? {
        return if (biome == ModBiomes.VOID_CHEST) {
            configured(IFeatureConfig.NONE)
        } else {
            null
        }
    }

    override fun configuredFlat(): StructureFeature<NoFeatureConfig, out Structure<NoFeatureConfig>> {
        return configured(IFeatureConfig.NONE)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        // no-op, this is only for structures generated via our AOTD quadtree structure mapper
        return false
    }

    override fun isFeatureChunk(
        chunkGenerator: ChunkGenerator,
        biomeProvider: BiomeProvider,
        seed: Long,
        random: SharedSeedRandom,
        centerChunkX: Int,
        centerChunkZ: Int,
        biome: Biome,
        featureChunkPos: ChunkPos,
        config: NoFeatureConfig
    ): Boolean {
        val xStart = centerChunkX * 16
        val xEnd = xStart + 15
        val amountOverMultipleOf1000 = xEnd % 1000

        return centerChunkZ * 16 == 0 && xStart >= 0 && amountOverMultipleOf1000 >= 16 && amountOverMultipleOf1000 < 32
    }
}