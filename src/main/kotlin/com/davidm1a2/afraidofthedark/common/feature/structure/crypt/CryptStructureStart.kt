package com.davidm1a2.afraidofthedark.common.feature.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.feature.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.structure.Structure
import kotlin.math.roundToInt

class CryptStructureStart(structure: Structure<*>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, xPos: Int, zPos: Int) {
        // The height of the structure = average of the 4 center corner's height
        val centerCorner1Height = generator.func_222532_b(xPos - 3, zPos - 3, Heightmap.Type.WORLD_SURFACE_WG)
        val centerCorner2Height = generator.func_222532_b(xPos + 3, zPos - 3, Heightmap.Type.WORLD_SURFACE_WG)
        val centerCorner3Height = generator.func_222532_b(xPos - 3, zPos + 3, Heightmap.Type.WORLD_SURFACE_WG)
        val centerCorner4Height = generator.func_222532_b(xPos + 3, zPos + 3, Heightmap.Type.WORLD_SURFACE_WG)
        val yPos = ((centerCorner1Height + centerCorner2Height + centerCorner3Height + centerCorner4Height) / 4.0).roundToInt()

        // Set the schematic height to be underground + 3 blocks+, ensure it isn't below bedrock
        val adjustedY = (yPos - ModSchematics.CRYPT.getHeight() + 3).coerceAtLeast(1)

        this.components.add(
            SchematicStructurePiece(
                chunkPosX * 16 - ModSchematics.CRYPT.getWidth() / 2,
                adjustedY,
                chunkPosZ * 16 - ModSchematics.CRYPT.getLength() / 2,
                this.rand,
                ModSchematics.CRYPT,
                ModLootTables.CRYPT
            )
        )
        this.recalculateStructureSize()
    }
}