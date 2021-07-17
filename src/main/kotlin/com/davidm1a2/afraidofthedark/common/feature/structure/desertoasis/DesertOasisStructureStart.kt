package com.davidm1a2.afraidofthedark.common.feature.structure.desertoasis

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.SchematicStructurePiece
import com.davidm1a2.afraidofthedark.common.feature.structure.base.getWorld
import com.davidm1a2.afraidofthedark.common.schematic.Schematic
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager
import kotlin.math.roundToInt

class DesertOasisStructureStart(structure: Structure<*>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    StructureStart(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, templateManagerIn: TemplateManager, centerChunkX: Int, centerChunkZ: Int, biomeIn: Biome) {
        val cornerPosX = chunkPosX * 16 - ModSchematics.DESERT_OASIS.getWidth() / 2
        val cornerPosY = (structure as AOTDStructure<*>).getEdgeHeights(chunkPosX * 16, chunkPosZ * 16, generator, generator.getWorld())
            .average()
            .roundToInt()
            .minus(18)
            .coerceIn(0, 255)
        val cornerPosZ = chunkPosZ * 16 - ModSchematics.DESERT_OASIS.getLength() / 2

        this.components.add(
            SchematicStructurePiece(
                cornerPosX,
                cornerPosY,
                cornerPosZ,
                rand,
                ModSchematics.DESERT_OASIS,
                ModLootTables.DESERT_OASIS,
                Direction.NORTH
            )
        )

        PlotTypes.values().forEach {
            val shuffledSchematics = it.schematics.indices.shuffled(rand)
            it.offsets.forEachIndexed { index, offset ->
                this.components.add(
                    SchematicStructurePiece(
                        cornerPosX + offset.x,
                        cornerPosY + offset.y,
                        cornerPosZ + offset.z,
                        rand,
                        it.schematics[shuffledSchematics[index]],
                        ModLootTables.DESERT_OASIS,
                        if (it != PlotTypes.Large && rand.nextBoolean()) it.facing.opposite else it.facing
                    )
                )
            }
        }

        this.recalculateStructureSize()
    }

    private enum class PlotTypes(val schematics: Array<Schematic>, val offsets: List<BlockPos>, val facing: Direction) {
        Small(
            ModSchematics.DESERT_OASIS_SMALL_PLOTS,
            listOf(
                BlockPos(154, 19, 141),
                BlockPos(93, 20, 161),
                BlockPos(132, 21, 78),
                BlockPos(43, 21, 99),
                BlockPos(35, 21, 136)
            ),
            Direction.NORTH
        ),
        Small90(
            ModSchematics.DESERT_OASIS_SMALL_PLOTS,
            listOf(
                BlockPos(37, 21, 22),
                BlockPos(10, 21, 77),
                BlockPos(164, 21, 101)
            ),
            Direction.EAST
        ),
        Medium(
            ModSchematics.DESERT_OASIS_MEDIUM_PLOTS,
            listOf(
                BlockPos(122, 22, 129),
                BlockPos(155, 24, 74),
                BlockPos(84, 25, 119)
            ),
            Direction.NORTH
        ),
        Medium90(
            ModSchematics.DESERT_OASIS_MEDIUM_PLOTS,
            listOf(
                BlockPos(113, 19, 61),
                BlockPos(45, 22, 42),
                BlockPos(76, 26, 57)
            ),
            Direction.EAST
        ),
        Large(
            ModSchematics.DESERT_OASIS_LARGE_PLOTS,
            listOf(
                BlockPos(132, 22, 31)
            ),
            Direction.NORTH
        );
    }
}