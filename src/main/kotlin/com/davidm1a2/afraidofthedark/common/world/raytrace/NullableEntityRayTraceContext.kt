package com.davidm1a2.afraidofthedark.common.world.raytrace

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceContext
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader

class NullableEntityRayTraceContext(startVec: Vec3d, endVec: Vec3d, private val blockMode: BlockMode, fluidMode: FluidMode, private val entity: Entity?) :
    RayTraceContext(startVec, endVec, blockMode, fluidMode, entity ?: DUMMY_ENTITY) {
    override fun getBlockShape(blockState: BlockState, world: IBlockReader, blockPos: BlockPos): VoxelShape {
        // This is an ugly hack, should be removed in 1.15
        return if (entity != null) {
            super.getBlockShape(blockState, world, blockPos)
        } else {
            blockMode.get(blockState, world, blockPos, ISelectionContext.dummy())
        }
    }

    companion object {
        private val DUMMY_ENTITY = ItemEntity(null, 0.0, 0.0, 0.0)
    }
}