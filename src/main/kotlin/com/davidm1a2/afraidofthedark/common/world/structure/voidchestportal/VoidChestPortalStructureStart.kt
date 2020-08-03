package com.davidm1a2.afraidofthedark.common.world.structure.voidchestportal

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.EnumFacing
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorldReaderBase
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.structure.StructureStart

class VoidChestPortalStructureStart : StructureStart {
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
        val posX = multipleOf1000 * 1000 + 16

        this.components.add(
            SchematicStructurePiece(
                posX + 4,
                100,
                -2,
                random,
                ModSchematics.VOID_CHEST_PORTAL,
                facing = EnumFacing.NORTH
            )
        )
        this.recalculateStructureSize(world)
    }
}