package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import com.davidm1a2.afraidofthedark.common.world.structure.base.getWorld
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager
import kotlin.math.roundToInt

class CryptStructureStart(structure: Structure<*>, chunkX: Int, chunkZ: Int, biomeIn: Biome, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    StructureStart(structure, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, templateManagerIn: TemplateManager, centerChunkX: Int, centerChunkZ: Int, biomeIn: Biome) {
        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val worldIn = generator.getWorld()

        // The height of the structure = average of the 4 center corner's height
        val centerCorner1Height = WorldHeightmap.getHeight(xPos - 3, zPos - 3, worldIn, generator)
        val centerCorner2Height = WorldHeightmap.getHeight(xPos + 3, zPos - 3, worldIn, generator)
        val centerCorner3Height = WorldHeightmap.getHeight(xPos - 3, zPos + 3, worldIn, generator)
        val centerCorner4Height = WorldHeightmap.getHeight(xPos + 3, zPos + 3, worldIn, generator)
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