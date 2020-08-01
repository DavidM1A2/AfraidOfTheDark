package com.davidm1a2.afraidofthedark.common.world.structure.darkforest

import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorldReaderBase
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.structure.StructureStart

class DarkForestStructureStart : StructureStart {
    // Required for reflection
    constructor() : super()

    constructor(world: IWorldReaderBase, chunkPosX: Int, y: Int, chunkPosZ: Int, biome: Biome, random: SharedSeedRandom, seed: Long) : super(
        chunkPosX,
        chunkPosZ,
        biome,
        random,
        seed
    ) {

        this.recalculateStructureSize(world)
    }
}