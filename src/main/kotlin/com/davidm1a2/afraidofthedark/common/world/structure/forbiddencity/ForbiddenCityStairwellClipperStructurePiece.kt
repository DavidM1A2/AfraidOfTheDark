package com.davidm1a2.afraidofthedark.common.world.structure.forbiddencity

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import com.davidm1a2.afraidofthedark.common.schematic.Schematic
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
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

class ForbiddenCityStairwellClipperStructurePiece : StructurePiece {
    constructor(nbt: CompoundTag) : super(ModStructures.FORBIDDEN_CITY_STAIRWELL_CLIPPER_STRUCTURE_PIECE, nbt)

    constructor(xPos: Int, groundY: Int, maxStairwellY: Int, zPos: Int, boundingBox: BoundingBox = BoundingBox(xPos,
        groundY, zPos, xPos + ModSchematics.STAIRWELL.getWidth() - 1, maxStairwellY,
        zPos + ModSchematics.STAIRWELL.getLength() - 1)) :
            super(ModStructures.FORBIDDEN_CITY_STAIRWELL_CLIPPER_STRUCTURE_PIECE, 0, boundingBox) {
        this.boundingBox = boundingBox
        this.orientation = Direction.NORTH
    }

    override fun postProcess(
        world: WorldGenLevel,
        structureManager: StructureFeatureManager,
        chunkGenerator: ChunkGenerator,
        random: Random,
        structureBoundingBox: BoundingBox,
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

    override fun addAdditionalSaveData(p_163551_: ServerLevel, p_143011_1_: CompoundTag) {
    }
}