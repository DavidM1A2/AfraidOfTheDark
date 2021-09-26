package com.davidm1a2.afraidofthedark.common.feature.structure.darkforest

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.feature.structure.base.MultiplierConfig
import com.davidm1a2.afraidofthedark.common.feature.structure.base.SchematicStructurePiece
import net.minecraft.util.Direction
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.structure.Structure
import java.util.Random
import kotlin.math.max
import kotlin.math.min

class DarkForestStructureStart(structure: Structure<MultiplierConfig>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart<MultiplierConfig>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int) {
        val bedHouseWidth = ModSchematics.BED_HOUSE.getWidth()
        val bedHouseLength = ModSchematics.BED_HOUSE.getLength()

        val darkForest = feature as AOTDStructure<*>

        val yPos = darkForest.getEdgeHeights(xPos, zPos, generator, bedHouseWidth.toInt(), bedHouseLength.toInt()).minOrNull()!! - 1

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

            val cornerHeight1 = generator.getBaseHeight(prop.boundingBox.x0, prop.boundingBox.z0, Heightmap.Type.WORLD_SURFACE_WG)
            val cornerHeight2 = generator.getBaseHeight(prop.boundingBox.x1, prop.boundingBox.z0, Heightmap.Type.WORLD_SURFACE_WG)
            val cornerHeight3 = generator.getBaseHeight(prop.boundingBox.x0, prop.boundingBox.z1, Heightmap.Type.WORLD_SURFACE_WG)
            val cornerHeight4 = generator.getBaseHeight(prop.boundingBox.x1, prop.boundingBox.z1, Heightmap.Type.WORLD_SURFACE_WG)
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

            val trunkHeight1 = generator.getBaseHeight(x + approximateTrunkWidth, z + approximateTrunkWidth, Heightmap.Type.WORLD_SURFACE_WG)
            val trunkHeight2 = generator.getBaseHeight(x + approximateTrunkWidth, z - approximateTrunkWidth, Heightmap.Type.WORLD_SURFACE_WG)
            val trunkHeight3 = generator.getBaseHeight(x - approximateTrunkWidth, z + approximateTrunkWidth, Heightmap.Type.WORLD_SURFACE_WG)
            val trunkHeight4 = generator.getBaseHeight(x - approximateTrunkWidth, z - approximateTrunkWidth, Heightmap.Type.WORLD_SURFACE_WG)
            val trunkHeightCenter = generator.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG)
            // Trees need to have roots underground so move them down by 5, ensure it's above ground though
            tree.updateY(min(min(min(trunkHeight1, trunkHeight2), min(trunkHeight3, trunkHeight4)), trunkHeightCenter) - 5)

            this.pieces.add(tree)
        }

        this.calculateBoundingBox()
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