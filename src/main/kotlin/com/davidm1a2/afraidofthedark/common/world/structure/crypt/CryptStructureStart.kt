package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorldReaderBase
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.structure.StructureStart

class CryptStructureStart : StructureStart {
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
                chunkPosX * 16 - ModSchematics.CRYPT.getWidth() / 2,
                y,
                chunkPosZ * 16 - ModSchematics.CRYPT.getLength() / 2,
                random,
                ModSchematics.CRYPT,
                ModLootTables.CRYPT
            )
        )
        this.recalculateStructureSize(world)
    }
}