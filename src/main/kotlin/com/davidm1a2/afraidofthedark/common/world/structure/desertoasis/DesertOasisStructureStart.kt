package com.davidm1a2.afraidofthedark.common.world.structure.desertoasis

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.StructureFeature
import kotlin.math.roundToInt

class DesertOasisStructureStart(structure: StructureFeature<BooleanConfig>, chunkPos: ChunkPos, referenceIn: Int, seed: Long) :
    AOTDStructureStart<BooleanConfig>(structure, chunkPos, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor) {
        val cornerPosX = xPos - ModSchematics.DESERT_OASIS.getWidth() / 2
        val cornerPosY = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator, levelHeightAccessor)
            .average()
            .roundToInt()
            .minus(18)
            .coerceIn(0, 255)
        val cornerPosZ = zPos - ModSchematics.DESERT_OASIS.getLength() / 2

        this.pieces.add(
            SchematicStructurePiece(
                cornerPosX,
                cornerPosY,
                cornerPosZ,
                random,
                ModSchematics.DESERT_OASIS,
                ModLootTables.DESERT_OASIS,
                Direction.NORTH
            )
        )

        PlotTypes.values().forEach {
            val shuffledSchematics = it.schematics.indices.shuffled(random)
            it.offsets.forEachIndexed { index, offset ->
                this.pieces.add(
                    SchematicStructurePiece(
                        cornerPosX + offset.x,
                        cornerPosY + offset.y,
                        cornerPosZ + offset.z,
                        random,
                        it.schematics[shuffledSchematics[index]],
                        ModLootTables.DESERT_OASIS,
                        if (it != PlotTypes.Large && random.nextBoolean()) it.facing.opposite else it.facing
                    )
                )
            }
        }

        this.createBoundingBox()
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