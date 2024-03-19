package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.feature.StructureFeature
import kotlin.math.roundToInt

class CryptStructureStart(structure: StructureFeature<MultiplierConfig>, chunkPos: ChunkPos, referenceIn: Int, seed: Long) :
    AOTDStructureStart<MultiplierConfig>(structure, chunkPos, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor) {
        // The height of the structure = average of the 4 center corner's height
        val centerCorner1Height = generator.getBaseHeight(xPos - 3, zPos - 3, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
        val centerCorner2Height = generator.getBaseHeight(xPos + 3, zPos - 3, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
        val centerCorner3Height = generator.getBaseHeight(xPos - 3, zPos + 3, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
        val centerCorner4Height = generator.getBaseHeight(xPos + 3, zPos + 3, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
        val yPos = ((centerCorner1Height + centerCorner2Height + centerCorner3Height + centerCorner4Height) / 4.0).roundToInt()

        // Set the schematic height to be underground + 3 blocks+, ensure it isn't below bedrock
        val adjustedY = (yPos - ModSchematics.CRYPT.getHeight() + 3).coerceAtLeast(1)

        this.pieces.add(
            SchematicStructurePiece(
                chunkPos.x * 16 - ModSchematics.CRYPT.getWidth() / 2,
                adjustedY,
                chunkPos.z * 16 - ModSchematics.CRYPT.getLength() / 2,
                this.random,
                ModSchematics.CRYPT,
                ModLootTables.CRYPT
            )
        )
        this.createBoundingBox()
    }
}