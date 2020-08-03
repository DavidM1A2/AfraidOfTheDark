package com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox

import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorldReaderBase
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.structure.StructureStart

class VoidChestBoxStructureStart : StructureStart {
    // Required for reflection
    constructor() : super()

    constructor(world: IWorldReaderBase, chunkPosX: Int, biome: Biome, random: SharedSeedRandom, seed: Long) : super(
        chunkPosX,
        0,
        biome,
        random,
        seed
    ) {
        val startX = chunkPosX * 16
        val endX = startX + 15
        val multipleOf1000 = endX / 1000
        val posX = multipleOf1000 * 1000

        this.components.add(VoidChestBoxStructurePiece(posX))
        this.recalculateStructureSize(world)
    }
}