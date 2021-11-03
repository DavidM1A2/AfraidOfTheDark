package com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.ISeedReader
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureManager
import net.minecraft.world.gen.feature.structure.StructurePiece
import java.util.Random

class VoidChestBoxStructurePiece : StructurePiece {
    constructor(nbt: CompoundNBT) : super(ModStructures.VOID_BOX_STRUCTURE_PIECE, nbt)

    constructor(xPos: Int) : super(ModStructures.VOID_BOX_STRUCTURE_PIECE, 0) {
        this.boundingBox = MutableBoundingBox(xPos, 100, 0, xPos + 48, 100 + 48, 0 + 48)
    }

    override fun postProcess(
        world: ISeedReader,
        structureManager: StructureManager,
        chunkGenerator: ChunkGenerator,
        random: Random,
        structureBoundingBox: MutableBoundingBox,
        chunkPos: ChunkPos,
        structureBottomCenter: BlockPos
    ): Boolean {
        val barrier = Blocks.BARRIER.defaultBlockState()
        val xPos = this.boundingBox.x0

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

    override fun addAdditionalSaveData(tagCompound: CompoundNBT) {
    }
}