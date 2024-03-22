package com.davidm1a2.afraidofthedark.common.world.structure.darkforest

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.core.Direction
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.feature.StructureFeature
import java.util.*
import kotlin.math.max
import kotlin.math.min

class DarkForestStructureStart(structure: StructureFeature<MultiplierConfig>, chunkPos: ChunkPos, referenceIn: Int, seed: Long) :
    AOTDStructureStart<MultiplierConfig>(structure, chunkPos, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor) {
        val bedHouseWidth = ModSchematics.BED_HOUSE.getWidth()
        val bedHouseLength = ModSchematics.BED_HOUSE.getLength()

        val darkForest = feature as AOTDStructure<*>

        val yPos = darkForest.getEdgeHeights(xPos, zPos, generator, levelHeightAccessor, bedHouseWidth.toInt(), bedHouseLength.toInt()).minOrNull()!! - 1

        val width = darkForest.getWidth()
        val length = darkForest.getLength()

        /*
        Dark forest layout:
           ________________________________
          |                s               |
          |            t gutter            | l
          |          ____________          | e
          |         |            |         | n
          | l gutter|   house    | r gutter| g
          |    w    |            |    e    | t
          |         |____________|         | h
          |               n                |
          |            b gutter            |
          |________________________________|
                        width
         */

        val topGutterWidth = (length - bedHouseLength) / 2
        val bottomGutterWidth = (length - bedHouseLength) / 2
        val leftGutterWidth = (width - bedHouseWidth) / 2
        val rightGutterWidth = (width - bedHouseWidth) / 2

        // Add the center house
        this.pieces.add(
            SchematicStructurePiece(
                xPos - bedHouseWidth / 2,
                yPos,
                zPos - bedHouseLength / 2,
                random,
                ModSchematics.BED_HOUSE,
                ModLootTables.DARK_FOREST
            )
        )

        // Add props
        val numProps = random.nextInt(MIN_PROPS, MAX_PROPS)
        for (ignored in 0..numProps) {
            val propSchematic = ModSchematics.DARK_FOREST_PROPS[random.nextInt(ModSchematics.DARK_FOREST_PROPS.size)]

            val widthLengthMax = max(propSchematic.getWidth().toInt(), propSchematic.getHeight().toInt())
            val gutter = Direction.Plane.HORIZONTAL.getRandomDirection(random)

            val (x, z) = when (gutter) {
                Direction.NORTH -> Pair(
                    xPos + random.nextInt(-width / 2, width / 2 - widthLengthMax),
                    zPos - bedHouseLength / 2 - random.nextInt(widthLengthMax, topGutterWidth)
                )
                Direction.SOUTH -> Pair(
                    xPos + random.nextInt(-width / 2, width / 2 - widthLengthMax),
                    zPos + bedHouseLength / 2 + random.nextInt(0, bottomGutterWidth - widthLengthMax)
                )
                Direction.WEST -> Pair(
                    xPos - bedHouseWidth / 2 - random.nextInt(widthLengthMax, leftGutterWidth),
                    zPos + random.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                Direction.EAST -> Pair(
                    xPos + bedHouseWidth / 2 + random.nextInt(0, rightGutterWidth - widthLengthMax),
                    zPos + random.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                else -> throw IllegalArgumentException("Got an invalid gutter $gutter, should not be possible.")
            }
            val prop = SchematicStructurePiece(x, 0, z, random, propSchematic)

            val cornerHeight1 = generator.getBaseHeight(prop.boundingBox.minX(), prop.boundingBox.minZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            val cornerHeight2 = generator.getBaseHeight(prop.boundingBox.maxX(), prop.boundingBox.minZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            val cornerHeight3 = generator.getBaseHeight(prop.boundingBox.minX(), prop.boundingBox.maxZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            val cornerHeight4 = generator.getBaseHeight(prop.boundingBox.maxX(), prop.boundingBox.maxZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            prop.updateY(min(min(cornerHeight1, cornerHeight2), min(cornerHeight3, cornerHeight4)))

            this.pieces.add(prop)
        }

        // Add trees
        val numTrees = random.nextInt(MIN_TREES, MAX_TREES)
        for (ignored in 0..numTrees) {
            val treeSchematic = ModSchematics.DARK_FOREST_TREES[random.nextInt(ModSchematics.DARK_FOREST_TREES.size)]

            val approximateTrunkWidth = 4
            val widthLengthMax = max(treeSchematic.getWidth().toInt(), treeSchematic.getHeight().toInt())
            val gutter = Direction.Plane.HORIZONTAL.getRandomDirection(random)

            val (x, z) = when (gutter) {
                Direction.NORTH -> Pair(
                    xPos + random.nextInt(-width / 2, width / 2 - widthLengthMax),
                    zPos - bedHouseLength / 2 - random.nextInt(approximateTrunkWidth + widthLengthMax / 2, bottomGutterWidth)
                )
                Direction.SOUTH -> Pair(
                    xPos + random.nextInt(-width / 2, width / 2 - widthLengthMax),
                    zPos + bedHouseLength / 2 + random.nextInt(approximateTrunkWidth - widthLengthMax / 2, topGutterWidth - widthLengthMax)
                )
                Direction.WEST -> Pair(
                    xPos - bedHouseWidth / 2 - random.nextInt(approximateTrunkWidth + widthLengthMax / 2, leftGutterWidth),
                    zPos + random.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                Direction.EAST -> Pair(
                    xPos + bedHouseWidth / 2 + random.nextInt(approximateTrunkWidth - widthLengthMax / 2, rightGutterWidth - widthLengthMax),
                    zPos + random.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                else -> throw IllegalArgumentException("Got an invalid gutter $gutter, should not be possible.")
            }

            val tree = SchematicStructurePiece(x, 0, z, random, treeSchematic)

            val trunkHeight1 = generator.getBaseHeight(x + approximateTrunkWidth, z + approximateTrunkWidth, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            val trunkHeight2 = generator.getBaseHeight(x + approximateTrunkWidth, z - approximateTrunkWidth, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            val trunkHeight3 = generator.getBaseHeight(x - approximateTrunkWidth, z + approximateTrunkWidth, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            val trunkHeight4 = generator.getBaseHeight(x - approximateTrunkWidth, z - approximateTrunkWidth, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            val trunkHeightCenter = generator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor)
            // Trees need to have roots underground so move them down by 5, ensure it's above ground though
            tree.updateY(min(min(min(trunkHeight1, trunkHeight2), min(trunkHeight3, trunkHeight4)), trunkHeightCenter) - 5)

            this.pieces.add(tree)
        }

        this.createBoundingBox()
    }

    private fun Random.nextInt(min: Int, max: Int): Int {
        require(max > min)
        return this.nextInt(max - min) + min
    }

    companion object {
        private const val MIN_PROPS = 35
        private const val MAX_PROPS = 75
        private const val MIN_TREES = 14
        private const val MAX_TREES = 20
    }
}