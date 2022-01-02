package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader

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
    override fun newBlockEntity(world: IBlockReader): TileEntity {
        return EnariasAltarTileEntity()
    }

    override fun getShape(state: BlockState, world: IBlockReader, blockPos: BlockPos, context: ISelectionContext): VoxelShape {
        return ENARIAS_ALTAR_SHAPE
    }

    companion object {
        // For some reason, MC decides to render sides of blocks based on the shape. If the hitbox is not a full cube, we render sides. Make this "almost" a full cube
        private val ENARIAS_ALTAR_SHAPE = Block.box(0.001, 0.0, 0.001, 15.999, 15.999, 15.999)
    }
}