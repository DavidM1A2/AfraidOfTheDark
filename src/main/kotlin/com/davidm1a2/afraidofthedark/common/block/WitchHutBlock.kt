package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.WitchHutTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

/**
 * Desert oasis block used to monitor the state of the desert oasis dungeon
 *
 * @constructor sets the block name and properties
 */
class WitchHutBlock : AOTDTileEntityBlock(
    "witch_hut",
    Properties.of(Material.STONE)
        .strength(10.0f, 50.0f)
) {
    override fun displayInCreative(): Boolean {
        return false
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return WitchHutTileEntity(blockPos, blockState)
    }

    override fun <T : BlockEntity?> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        return if (level.isClientSide) {
            null
        } else {
            createTickerHelper(type, ModTileEntities.WITCH_HUT, WitchHutTileEntity::serverTick)
        }
    }
}