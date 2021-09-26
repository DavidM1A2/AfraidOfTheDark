package com.davidm1a2.afraidofthedark.common.feature.structure.gnomishcity

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.ISeedReader
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureManager
import net.minecraft.world.gen.feature.structure.StructurePiece
import java.util.Random

class GnomishCityStairwellClipperStructurePiece : StructurePiece {
    constructor(nbt: CompoundNBT) : super(ModStructures.GNOMISH_CITY_STAIRWELL_CLIPPER_STRUCTURE_PIECE, nbt)

    constructor(xPos: Int, groundY: Int, maxStairwellY: Int, zPos: Int) : super(ModStructures.GNOMISH_CITY_STAIRWELL_CLIPPER_STRUCTURE_PIECE, 0) {
        val stairwell = ModSchematics.STAIRWELL
        this.boundingBox = MutableBoundingBox(xPos, groundY, zPos, xPos + stairwell.getWidth() - 1, maxStairwellY, zPos + stairwell.getLength() - 1)
        this.orientation = Direction.NORTH
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
        val stairwell = ModSchematics.STAIRWELL
        for (x in 0 until stairwell.getWidth()) {
            for (y in 0 until this.boundingBox.ySpan) {
                for (z in 0 until stairwell.getLength()) {
                    placeBlock(world, Blocks.AIR.defaultBlockState(), x, y, z, structureBoundingBox)
                }
            }
        }
        return true
    }

    override fun addAdditionalSaveData(p_143011_1_: CompoundNBT) {
    }
}