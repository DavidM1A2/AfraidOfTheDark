package com.davidm1a2.afraidofthedark.common.dimension.nightmare

import net.minecraft.entity.EntityClassification
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.*
import net.minecraft.world.server.ServerWorld

/**
 * Chunk generator for the nightmare dimension
 *
 * @param world The nightmare world instance
 * @param biomeProvider The biome provider to use
 * @param settings The generation settings
 */
class NightmareChunkGenerator(world: IWorld, biomeProvider: BiomeProvider, settings: GenerationSettings) :
    ChunkGenerator<GenerationSettings>(world, biomeProvider, settings) {
    override fun carve(chunk: IChunk, stage: GenerationStage.Carving) {
        // Do nothing, there's nothing to carve out of void
    }

    override fun getPossibleCreatures(creatureType: EntityClassification, pos: BlockPos): List<SpawnListEntry> {
        return emptyList()
    }

    override fun getGroundHeight(): Int {
        // Spawn players at sky height, this doesnt matter since we teleport the player on spawn/death
        return 256
    }

    override fun makeBase(world: IWorld, chunk: IChunk) {
        // chunk.createHeightMap()
        chunk.func_217303_b(Heightmap.Type.WORLD_SURFACE_WG)
        chunk.func_217303_b(Heightmap.Type.OCEAN_FLOOR_WG)
    }

    override fun generateSurface(chunk: IChunk) {
    }

    override fun func_222529_a(chunkX: Int, chunkZ: Int, heightmap: Heightmap.Type): Int {
        return 0
    }

    override fun spawnMobs(p0: WorldGenRegion) {
        // Ignore
    }

    override fun spawnMobs(p0: ServerWorld, p1: Boolean, p2: Boolean) {
        // Ignore
    }
}