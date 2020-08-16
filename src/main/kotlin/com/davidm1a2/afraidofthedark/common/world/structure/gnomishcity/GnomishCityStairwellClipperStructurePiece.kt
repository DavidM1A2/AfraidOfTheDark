package com.davidm1a2.afraidofthedark.common.world.structure.gnomishcity

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.structure.StructurePiece
import net.minecraft.world.gen.feature.template.TemplateManager
import java.util.*

class GnomishCityStairwellClipperStructurePiece() : StructurePiece() {
    constructor(xPos: Int, groundY: Int, maxStairwellY: Int, zPos: Int) : this() {
        val stairwell = ModSchematics.STAIRWELL
        this.boundingBox = MutableBoundingBox(xPos, groundY, zPos, xPos + stairwell.getWidth() - 1, maxStairwellY, zPos + stairwell.getLength() - 1)
        this.coordBaseMode = EnumFacing.NORTH
    }

    override fun addComponentParts(worldIn: IWorld, randomIn: Random, structureBoundingBoxIn: MutableBoundingBox, chunkPos: ChunkPos): Boolean {
        val stairwell = ModSchematics.STAIRWELL
        for (x in 0 until stairwell.getWidth()) {
            for (y in 0 until this.boundingBox.ySize) {
                for (z in 0 until stairwell.getLength()) {
                    setBlockState(worldIn, Blocks.AIR.defaultState, x, y, z, structureBoundingBoxIn)
                }
            }
        }
        return true
    }

    override fun writeStructureToNBT(tagCompound: NBTTagCompound) {
    }

    override fun readStructureFromNBT(tagCompound: NBTTagCompound, p_143011_2_: TemplateManager) {
    }
}