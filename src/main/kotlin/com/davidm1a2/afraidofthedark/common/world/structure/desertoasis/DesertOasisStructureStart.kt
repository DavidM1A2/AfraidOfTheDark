package com.davidm1a2.afraidofthedark.common.world.structure.desertoasis

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.EnumFacing
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.structure.StructureStart

class DesertOasisStructureStart : StructureStart {
    // Required for reflection
    constructor() : super()

    constructor(
        world: IBlockReader,
        chunkPosX: Int,
        yPos: Int,
        chunkPosZ: Int,
        biome: Biome,
        random: SharedSeedRandom,
        seed: Long
    ) : super(
        chunkPosX,
        chunkPosZ,
        biome,
        random,
        seed
    ) {
        val cornerPosX = chunkPosX * 16 - ModSchematics.DESERT_OASIS.getWidth() / 2
        val cornerPosY = yPos
        val cornerPosZ = chunkPosZ * 16 - ModSchematics.DESERT_OASIS.getLength() / 2

        this.components.add(
            SchematicStructurePiece(
                cornerPosX,
                cornerPosY,
                cornerPosZ,
                random,
                ModSchematics.DESERT_OASIS,
                ModLootTables.DESERT_OASIS,
                EnumFacing.NORTH
            )
        )

        PlotTypes.values().forEach { plot ->
            val shuffledSchematics = plot.schematics.indices.shuffled(random)
            plot.offsets.forEachIndexed { index, offset ->
                this.components.add(
                    SchematicStructurePiece(
                        cornerPosX + offset.x,
                        cornerPosY + offset.y,
                        cornerPosZ + offset.z,
                        random,
                        plot.schematics[shuffledSchematics[index]],
                        ModLootTables.DESERT_OASIS,
                        EnumFacing.NORTH
                    )
                )
            }
        }

        this.recalculateStructureSize(world)
    }

    private enum class PlotTypes(internal val schematics: Array<Schematic>, internal val offsets: List<BlockPos>) {
        Small(
            ModSchematics.DESERT_OASIS_SMALL_PLOTS,
            listOf(
                BlockPos(154, 19, 141),
                BlockPos(93, 20, 161),
                BlockPos(132, 21, 78),
                BlockPos(43, 21, 99),
                BlockPos(35, 21, 136)
            )
        ),
        Small90(
            ModSchematics.DESERT_OASIS_SMALL_PLOTS90,
            listOf(
                BlockPos(37, 21, 22),
                BlockPos(10, 21, 77),
                BlockPos(164, 21, 101)
            )
        ),
        Medium(
            ModSchematics.DESERT_OASIS_MEDIUM_PLOTS,
            listOf(
                BlockPos(122, 22, 129),
                BlockPos(155, 24, 74),
                BlockPos(84, 25, 119)
            )
        ),
        Medium90(
            ModSchematics.DESERT_OASIS_MEDIUM_PLOTS90,
            listOf(
                BlockPos(113, 19, 61),
                BlockPos(45, 22, 42),
                BlockPos(76, 26, 57)
            )
        ),
        Large(
            ModSchematics.DESERT_OASIS_LARGE_PLOTS,
            listOf(
                BlockPos(132, 22, 31)
            )
        );
    }
}