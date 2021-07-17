package com.davidm1a2.afraidofthedark.common.feature.structure.gnomishcity

import com.davidm1a2.afraidofthedark.common.constants.ModFeatures
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Direction
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.StructurePiece
import java.util.*

class GnomishCityStairwellClipperStructurePiece : StructurePiece {
    constructor(nbt: CompoundNBT) : super(ModFeatures.GNOMISH_CITY_STAIRWELL_CLIPPER_STRUCTURE_PIECE, nbt)

    constructor(xPos: Int, groundY: Int, maxStairwellY: Int, zPos: Int) : super(ModFeatures.GNOMISH_CITY_STAIRWELL_CLIPPER_STRUCTURE_PIECE, 0) {
        val stairwell = ModSchematics.STAIRWELL
        this.boundingBox = MutableBoundingBox(xPos, groundY, zPos, xPos + stairwell.getWidth() - 1, maxStairwellY, zPos + stairwell.getLength() - 1)
        this.coordBaseMode = Direction.NORTH
    }

    override fun create(
        worldIn: IWorld,
        chunkGenerator: ChunkGenerator<*>,
        randomIn: Random,
        structureBoundingBoxIn: MutableBoundingBox,
        chunkPos: ChunkPos
    ): Boolean {
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

    override fun readAdditional(tagCompound: CompoundNBT) {
    }
}