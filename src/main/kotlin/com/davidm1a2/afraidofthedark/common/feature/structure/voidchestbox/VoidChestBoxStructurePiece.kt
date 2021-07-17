package com.davidm1a2.afraidofthedark.common.feature.structure.voidchestbox

import com.davidm1a2.afraidofthedark.common.constants.ModFeatures
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.StructurePiece
import java.util.*

class VoidChestBoxStructurePiece : StructurePiece {
    constructor(nbt: CompoundNBT) : super(ModFeatures.VOID_BOX_STRUCTURE_PIECE, nbt)

    constructor(xPos: Int) : super(ModFeatures.VOID_BOX_STRUCTURE_PIECE, 0) {
        this.boundingBox = MutableBoundingBox(xPos, 100, 0, xPos + 48, 100 + 48, 0 + 48)
    }

    override fun create(
        worldIn: IWorld,
        chunkGenerator: ChunkGenerator<*>,
        randomIn: Random,
        structureBoundingBoxIn: MutableBoundingBox,
        chunkPos: ChunkPos
    ): Boolean {
        val barrier = Blocks.BARRIER.defaultState
        val xPos = this.boundingBox.minX

        for (i in 0..48) {
            for (j in 0..48) {
                // Create the floor
                setBlockState(worldIn, barrier, xPos + i, 100, j, structureBoundingBoxIn)
                // Create the roof
                setBlockState(worldIn, barrier, xPos + i, 100 + 48, j, structureBoundingBoxIn)
                // Create the left wall
                setBlockState(worldIn, barrier, xPos + 0, 100 + i, j, structureBoundingBoxIn)
                // Create the right wall
                setBlockState(worldIn, barrier, xPos + 48, 100 + i, j, structureBoundingBoxIn)
                // Create the front wall
                setBlockState(worldIn, barrier, xPos + i, 100 + j, 0, structureBoundingBoxIn)
                // Create the back wall
                setBlockState(worldIn, barrier, xPos + i, 100 + j, 48, structureBoundingBoxIn)
            }
        }

        return true
    }

    override fun readAdditional(tagCompound: CompoundNBT) {
    }
}