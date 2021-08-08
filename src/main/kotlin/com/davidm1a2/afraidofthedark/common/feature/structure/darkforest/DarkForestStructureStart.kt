package com.davidm1a2.afraidofthedark.common.feature.structure.darkforest

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.SchematicStructurePiece
import net.minecraft.util.Direction
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.structure.Structure
import java.util.*
import kotlin.math.max
import kotlin.math.min

class DarkForestStructureStart(structure: Structure<*>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, xPos: Int, zPos: Int) {
        val bedHouseWidth = ModSchematics.BED_HOUSE.getWidth()
        val bedHouseLength = ModSchematics.BED_HOUSE.getLength()

        val darkForest = structure as AOTDStructure<*>

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
        this.components.add(
            SchematicStructurePiece(
                xPos - bedHouseWidth / 2,
                yPos,
                zPos - bedHouseLength / 2,
                rand,
                ModSchematics.BED_HOUSE,
                ModLootTables.DARK_FOREST
            )
        )

        // Add props
        val numProps = rand.nextInt(MIN_PROPS, MAX_PROPS)
        for (ignored in 0..numProps) {
            val propSchematic = ModSchematics.DARK_FOREST_PROPS[rand.nextInt(ModSchematics.DARK_FOREST_PROPS.size)]

            val widthLengthMax = max(propSchematic.getWidth().toInt(), propSchematic.getHeight().toInt())
            val gutter = Direction.Plane.HORIZONTAL.random(rand)

            val (x, z) = when (gutter) {
                Direction.NORTH -> Pair(
                    xPos + rand.nextInt(-width / 2, width / 2 - widthLengthMax),
                    zPos - bedHouseLength / 2 - rand.nextInt(widthLengthMax, topGutterWidth)
                )
                Direction.SOUTH -> Pair(
                    xPos + rand.nextInt(-width / 2, width / 2 - widthLengthMax),
                    zPos + bedHouseLength / 2 + rand.nextInt(0, bottomGutterWidth - widthLengthMax)
                )
                Direction.WEST -> Pair(
                    xPos - bedHouseWidth / 2 - rand.nextInt(widthLengthMax, leftGutterWidth),
                    zPos + rand.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                Direction.EAST -> Pair(
                    xPos + bedHouseWidth / 2 + rand.nextInt(0, rightGutterWidth - widthLengthMax),
                    zPos + rand.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                else -> throw IllegalArgumentException("Got an invalid gutter $gutter, should not be possible.")
            }
            val prop = SchematicStructurePiece(x, 0, z, rand, propSchematic)

            val cornerHeight1 = generator.func_222532_b(prop.boundingBox.minX, prop.boundingBox.minZ, Heightmap.Type.WORLD_SURFACE_WG)
            val cornerHeight2 = generator.func_222532_b(prop.boundingBox.maxX, prop.boundingBox.minZ, Heightmap.Type.WORLD_SURFACE_WG)
            val cornerHeight3 = generator.func_222532_b(prop.boundingBox.minX, prop.boundingBox.maxZ, Heightmap.Type.WORLD_SURFACE_WG)
            val cornerHeight4 = generator.func_222532_b(prop.boundingBox.maxX, prop.boundingBox.maxZ, Heightmap.Type.WORLD_SURFACE_WG)
            prop.updateY(min(min(cornerHeight1, cornerHeight2), min(cornerHeight3, cornerHeight4)))

            this.components.add(prop)
        }

        // Add trees
        val numTrees = rand.nextInt(MIN_TREES, MAX_TREES)
        for (ignored in 0..numTrees) {
            val treeSchematic = ModSchematics.DARK_FOREST_TREES[rand.nextInt(ModSchematics.DARK_FOREST_TREES.size)]

            val approximateTrunkWidth = 4
            val widthLengthMax = max(treeSchematic.getWidth().toInt(), treeSchematic.getHeight().toInt())
            val gutter = Direction.Plane.HORIZONTAL.random(rand)

            val (x, z) = when (gutter) {
                Direction.NORTH -> Pair(
                    xPos + rand.nextInt(-width / 2, width / 2 - widthLengthMax),
                    zPos - bedHouseLength / 2 - rand.nextInt(approximateTrunkWidth + widthLengthMax / 2, bottomGutterWidth)
                )
                Direction.SOUTH -> Pair(
                    xPos + rand.nextInt(-width / 2, width / 2 - widthLengthMax),
                    zPos + bedHouseLength / 2 + rand.nextInt(approximateTrunkWidth - widthLengthMax / 2, topGutterWidth - widthLengthMax)
                )
                Direction.WEST -> Pair(
                    xPos - bedHouseWidth / 2 - rand.nextInt(approximateTrunkWidth + widthLengthMax / 2, leftGutterWidth),
                    zPos + rand.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                Direction.EAST -> Pair(
                    xPos + bedHouseWidth / 2 + rand.nextInt(approximateTrunkWidth - widthLengthMax / 2, rightGutterWidth - widthLengthMax),
                    zPos + rand.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                else -> throw IllegalArgumentException("Got an invalid gutter $gutter, should not be possible.")
            }

            val tree = SchematicStructurePiece(x, 0, z, rand, treeSchematic)

            val trunkHeight1 = generator.func_222532_b(x + approximateTrunkWidth, z + approximateTrunkWidth, Heightmap.Type.WORLD_SURFACE_WG)
            val trunkHeight2 = generator.func_222532_b(x + approximateTrunkWidth, z - approximateTrunkWidth, Heightmap.Type.WORLD_SURFACE_WG)
            val trunkHeight3 = generator.func_222532_b(x - approximateTrunkWidth, z + approximateTrunkWidth, Heightmap.Type.WORLD_SURFACE_WG)
            val trunkHeight4 = generator.func_222532_b(x - approximateTrunkWidth, z - approximateTrunkWidth, Heightmap.Type.WORLD_SURFACE_WG)
            val trunkHeightCenter = generator.func_222532_b(x, z, Heightmap.Type.WORLD_SURFACE_WG)
            // Trees need to have roots underground so move them down by 5, ensure it's above ground though
            tree.updateY(min(min(min(trunkHeight1, trunkHeight2), min(trunkHeight3, trunkHeight4)), trunkHeightCenter) - 5)

            this.components.add(tree)
        }

        this.recalculateStructureSize()
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