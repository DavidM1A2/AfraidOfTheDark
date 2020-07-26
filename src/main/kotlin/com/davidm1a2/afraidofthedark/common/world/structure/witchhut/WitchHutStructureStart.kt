package com.davidm1a2.afraidofthedark.common.world.structure.witchhut

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorldReaderBase
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.structure.StructureStart

class WitchHutStructureStart : StructureStart {
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
                chunkPosX * 16 - ModSchematics.WITCH_HUT.getWidth() / 2,
                y,
                chunkPosZ * 16 - ModSchematics.WITCH_HUT.getLength() / 2,
                random,
                ModSchematics.WITCH_HUT,
                ModLootTables.WITCH_HUT
            )
        )
        this.recalculateStructureSize(world)
    }
}