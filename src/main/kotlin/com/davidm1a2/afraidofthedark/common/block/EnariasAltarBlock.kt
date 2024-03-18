package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

/**
 * Enaria's altar block used in the nightmare to being crafting spells
 *
 * @constructor sets the block's properties
 */
class EnariasAltarBlock : AOTDTileEntityBlock(
    "enarias_altar",
    Properties.of(Material.PORTAL)
        .lightLevel { 1 }
        .strength(50.0f, 1200.0f)
) {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return EnariasAltarTileEntity()
    }

    override fun getShape(state: BlockState, world: BlockGetter, blockPos: BlockPos, context: CollisionContext): VoxelShape {
        return ENARIAS_ALTAR_SHAPE
    }

    companion object {
        // For some reason, MC decides to render sides of blocks based on the shape. If the hitbox is not a full cube, we render sides. Make this "almost" a full cube
        private val ENARIAS_ALTAR_SHAPE = Block.box(0.001, 0.0, 0.001, 15.999, 15.999, 15.999)
    }
}