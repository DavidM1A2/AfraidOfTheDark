package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class FrostPhoenixSpawnerTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDTickingTileEntity(ModTileEntities.FROST_PHOENIX_SPAWNER, blockPos, blockState) {
    private var phoenixIsAlive = false
    private var respawnTime = 0L

    fun reportPhoenixGone() {
        phoenixIsAlive = false
        respawnTime = ticksExisted + RESPAWN_COOLDOWN_TICKS
    }

    override fun load(compound: CompoundTag) {
        super.load(compound)
        phoenixIsAlive = compound.getBoolean(NBT_PHOENIX_IS_ALIVE)
        respawnTime = compound.getLong(NBT_RESPAWN_TIME)
    }

    override fun save(compound: CompoundTag): CompoundTag {
        super.save(compound)
        compound.putBoolean(NBT_PHOENIX_IS_ALIVE, phoenixIsAlive)
        compound.putLong(NBT_RESPAWN_TIME, respawnTime)
        return compound
    }

    companion object {
        private const val NBT_PHOENIX_IS_ALIVE = "phoenix_is_alive"
        private const val NBT_RESPAWN_TIME = "respawn_time"

        // 30 seconds before it can respawn
        private const val RESPAWN_COOLDOWN_TICKS = 20 * 30 * 1

        fun serverTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: FrostPhoenixSpawnerTileEntity) {
            baseTick(level, blockPos, blockState, blockEntity)
            if (!blockEntity.phoenixIsAlive) {
                if (blockEntity.ticksExisted >= blockEntity.respawnTime) {
                    spawnFrostPhoenix(level, blockPos, blockEntity)
                }
            }
        }

        private fun spawnFrostPhoenix(level: Level, blockPos: BlockPos, blockEntity: FrostPhoenixSpawnerTileEntity) {
            blockEntity.phoenixIsAlive = true
            val phoenix = FrostPhoenixEntity(level, blockPos)
            phoenix.setPos(blockPos.x + 0.5, blockPos.y + 2.0, blockPos.z + 0.5)
            level.addFreshEntity(phoenix)
        }
    }
}