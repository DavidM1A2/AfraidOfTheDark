package com.davidm1a2.afraidofthedark.common.world.structure.nightmareisland

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.core.Direction
import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.structure.StructureStart
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager

class NightmareIslandStructureStart(
    structure: StructureFeature<NoneFeatureConfiguration>,
    chunkPos: ChunkPos,
    referenceIn: Int,
    seed: Long
) : StructureStart<NoneFeatureConfiguration>(structure, chunkPos, referenceIn, seed) {
    override fun generatePieces(
        registryAccess: RegistryAccess,
        chunkGenerator: ChunkGenerator,
        templateManagerIn: StructureManager,
        chunkPos: ChunkPos,
        biomeIn: Biome,
        config: NoneFeatureConfiguration,
        levelHeightAccessor: LevelHeightAccessor
    ) {
        val startX = chunkPos.x * 16
        val endX = startX + 15
        val multipleOf1000 = endX / 1000
        val posX = multipleOf1000 * 1000

        this.pieces.add(
            SchematicStructurePiece(
                posX,
                0,
                0,
                random,
                ModSchematics.NIGHTMARE_ISLAND,
                ModLootTables.NIGHTMARE_ISLAND,
                facing = Direction.NORTH
            )
        )
        this.createBoundingBox()
    }
}