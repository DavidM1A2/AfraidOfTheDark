package com.davidm1a2.afraidofthedark.common.world.structure.darkforest

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.EnumFacing
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureStart
import java.util.*
import kotlin.math.max
import kotlin.math.min

class DarkForestStructureStart : StructureStart {
    // Required for reflection
    constructor() : super()

    constructor(
        world: IWorld,
        chunkGen: IChunkGenerator<*>,
        chunkPosX: Int,
        y: Int,
        chunkPosZ: Int,
        biome: Biome,
        random: SharedSeedRandom,
        seed: Long,
        width: Int,
        length: Int
    ) : super(
        chunkPosX,
        chunkPosZ,
        biome,
        random,
        seed
    ) {
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

        val bedHouseWidth = ModSchematics.BED_HOUSE.getWidth()
        val bedHouseLength = ModSchematics.BED_HOUSE.getLength()

        val posX = chunkPosX * 16
        val posZ = chunkPosZ * 16

        val topGutterWidth = (length - bedHouseLength) / 2
        val bottomGutterWidth = (length - bedHouseLength) / 2
        val leftGutterWidth = (width - bedHouseWidth) / 2
        val rightGutterWidth = (width - bedHouseWidth) / 2

        // Add the center house
        this.components.add(
            SchematicStructurePiece(
                posX - bedHouseWidth / 2,
                y,
                posZ - bedHouseLength / 2,
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
            val gutter = EnumFacing.Plane.HORIZONTAL.random(random)

            val (x, z) = when (gutter) {
                EnumFacing.NORTH -> Pair(
                    posX + random.nextInt(-width / 2, width / 2 - widthLengthMax),
                    posZ - bedHouseLength / 2 - random.nextInt(widthLengthMax, topGutterWidth)
                )
                EnumFacing.SOUTH -> Pair(
                    posX + random.nextInt(-width / 2, width / 2 - widthLengthMax),
                    posZ + bedHouseLength / 2 + random.nextInt(0, bottomGutterWidth - widthLengthMax)
                )
                EnumFacing.WEST -> Pair(
                    posX - bedHouseWidth / 2 - random.nextInt(widthLengthMax, leftGutterWidth),
                    posZ + random.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                EnumFacing.EAST -> Pair(
                    posX + bedHouseWidth / 2 + random.nextInt(0, rightGutterWidth - widthLengthMax),
                    posZ + random.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                else -> throw IllegalArgumentException("Got an invalid gutter $gutter, should not be possible.")
            }
            val prop = SchematicStructurePiece(x, 0, z, random, propSchematic)

            val cornerHeight1 = WorldHeightmap.getHeight(prop.boundingBox.minX, prop.boundingBox.minZ, world, chunkGen)
            val cornerHeight2 = WorldHeightmap.getHeight(prop.boundingBox.maxX, prop.boundingBox.minZ, world, chunkGen)
            val cornerHeight3 = WorldHeightmap.getHeight(prop.boundingBox.minX, prop.boundingBox.maxZ, world, chunkGen)
            val cornerHeight4 = WorldHeightmap.getHeight(prop.boundingBox.maxX, prop.boundingBox.maxZ, world, chunkGen)
            prop.updateY(min(min(cornerHeight1, cornerHeight2), min(cornerHeight3, cornerHeight4)))

            this.components.add(prop)
        }

        // Add trees
        val numTrees = random.nextInt(MIN_TREES, MAX_TREES)
        for (ignored in 0..numTrees) {
            val treeSchematic = ModSchematics.DARK_FOREST_TREES[random.nextInt(ModSchematics.DARK_FOREST_TREES.size)]

            val approximateTrunkWidth = 4
            val widthLengthMax = max(treeSchematic.getWidth().toInt(), treeSchematic.getHeight().toInt())
            val gutter = EnumFacing.Plane.HORIZONTAL.random(random)

            val (x, z) = when (gutter) {
                EnumFacing.NORTH -> Pair(
                    posX + random.nextInt(-width / 2, width / 2 - widthLengthMax),
                    posZ - bedHouseLength / 2 - random.nextInt(approximateTrunkWidth + widthLengthMax / 2, bottomGutterWidth)
                )
                EnumFacing.SOUTH -> Pair(
                    posX + random.nextInt(-width / 2, width / 2 - widthLengthMax),
                    posZ + bedHouseLength / 2 + random.nextInt(approximateTrunkWidth - widthLengthMax / 2, topGutterWidth - widthLengthMax)
                )
                EnumFacing.WEST -> Pair(
                    posX - bedHouseWidth / 2 - random.nextInt(approximateTrunkWidth + widthLengthMax / 2, leftGutterWidth),
                    posZ + random.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                EnumFacing.EAST -> Pair(
                    posX + bedHouseWidth / 2 + random.nextInt(approximateTrunkWidth - widthLengthMax / 2, rightGutterWidth - widthLengthMax),
                    posZ + random.nextInt(-length / 2, length / 2 - widthLengthMax)
                )
                else -> throw IllegalArgumentException("Got an invalid gutter $gutter, should not be possible.")
            }

            val tree = SchematicStructurePiece(x, 0, z, random, treeSchematic)

            val trunkHeight1 = WorldHeightmap.getHeight(x + approximateTrunkWidth, z + approximateTrunkWidth, world, chunkGen)
            val trunkHeight2 = WorldHeightmap.getHeight(x + approximateTrunkWidth, z - approximateTrunkWidth, world, chunkGen)
            val trunkHeight3 = WorldHeightmap.getHeight(x - approximateTrunkWidth, z + approximateTrunkWidth, world, chunkGen)
            val trunkHeight4 = WorldHeightmap.getHeight(x - approximateTrunkWidth, z - approximateTrunkWidth, world, chunkGen)
            val trunkHeightCenter = WorldHeightmap.getHeight(x, z, world, chunkGen)
            // Trees need to have roots underground so move them down by 5, ensure it's above ground though
            tree.updateY(min(min(min(trunkHeight1, trunkHeight2), min(trunkHeight3, trunkHeight4)), trunkHeightCenter) - 5)

            this.components.add(tree)
        }

        this.recalculateStructureSize(world)
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