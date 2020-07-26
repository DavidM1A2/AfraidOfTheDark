package com.davidm1a2.afraidofthedark.common.world.structure.observatory

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorldReaderBase
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.structure.StructureStart

class ObservatoryStructureStart : StructureStart {
    // Required for reflection
    constructor() : super()

    constructor(world: IWorldReaderBase, chunkPosX: Int, y: Int, chunkPosZ: Int, biome: Biome, random: SharedSeedRandom, seed: Long) : super(
        chunkPosX,
        chunkPosZ,
        biome,
        random,
        seed
    ) {
        this.components.add(
            SchematicStructurePiece(
                chunkPosX * 16 - ModSchematics.OBSERVATORY.getWidth() / 2,
                y,
                chunkPosZ * 16 - ModSchematics.OBSERVATORY.getLength() / 2,
                random,
                ModSchematics.OBSERVATORY,
                ModLootTables.OBSERVATORY
            )
        )
        this.recalculateStructureSize(world)
    }
}