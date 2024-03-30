package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDTileEntityBlock
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.FrostPhoenixSpawnerTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

class FrostPhoenixSpawnerBlock : AOTDTileEntityBlock(
    "frost_phoenix_spawner",
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
        return FrostPhoenixSpawnerTileEntity(blockPos, blockState)
    }

    override fun <T : BlockEntity?> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        return if (level.isClientSide) {
            null
        } else {
            createTickerHelper(type, ModTileEntities.FROST_PHOENIX_SPAWNER, FrostPhoenixSpawnerTileEntity::serverTick)
        }
    }
}