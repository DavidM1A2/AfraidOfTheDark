package com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.StructureFeatureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.StructurePiece
import java.util.*

class VoidChestBoxStructurePiece : StructurePiece {
    constructor(nbt: CompoundTag) : super(ModStructures.VOID_BOX_STRUCTURE_PIECE, nbt)

    constructor(xPos: Int) : super(ModStructures.VOID_BOX_STRUCTURE_PIECE, 0, BoundingBox(xPos, 100, 0, xPos + 48, 100 + 48, 0 + 48))

    override fun postProcess(
        world: WorldGenLevel,
        structureManager: StructureFeatureManager,
        chunkGenerator: ChunkGenerator,
        random: Random,
        structureBoundingBox: BoundingBox,
        chunkPos: ChunkPos,
        structureBottomCenter: BlockPos
    ): Boolean {
        val barrier = Blocks.BARRIER.defaultBlockState()
        val xPos = this.boundingBox.minX()

        for (i in 0..48) {
            for (j in 0..48) {
                // Create the floor
                placeBlock(world, barrier, xPos + i, 100, j, structureBoundingBox)
                // Create the roof
                placeBlock(world, barrier, xPos + i, 100 + 48, j, structureBoundingBox)
                // Create the left wall
                placeBlock(world, barrier, xPos + 0, 100 + i, j, structureBoundingBox)
                // Create the right wall
                placeBlock(world, barrier, xPos + 48, 100 + i, j, structureBoundingBox)
                // Create the front wall
                placeBlock(world, barrier, xPos + i, 100 + j, 0, structureBoundingBox)
                // Create the back wall
                placeBlock(world, barrier, xPos + i, 100 + j, 48, structureBoundingBox)
            }
        }

        return true
    }

    override fun addAdditionalSaveData(level: ServerLevel, compoundTag: CompoundTag) {
    }
}