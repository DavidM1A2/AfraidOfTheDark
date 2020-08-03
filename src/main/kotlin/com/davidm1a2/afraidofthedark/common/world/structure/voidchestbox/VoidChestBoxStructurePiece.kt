package com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox

import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.structure.StructurePiece
import net.minecraft.world.gen.feature.template.TemplateManager
import java.util.*

class VoidChestBoxStructurePiece() : StructurePiece() {
    constructor(xPos: Int) : this() {
        this.boundingBox = MutableBoundingBox(xPos, 100, 0, xPos + 48, 100 + 48, 0 + 48)
    }

    override fun addComponentParts(worldIn: IWorld, randomIn: Random, structureBoundingBoxIn: MutableBoundingBox, chunkPos: ChunkPos): Boolean {
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

    override fun writeStructureToNBT(tagCompound: NBTTagCompound) {
    }

    override fun readStructureFromNBT(tagCompound: NBTTagCompound, p_143011_2_: TemplateManager) {
    }
}