package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT

class FrostPhoenixSpawnerTileEntity : AOTDTickingTileEntity(ModTileEntities.FROST_PHOENIX_SPAWNER) {
    private var phoenixIsAlive = false
    private var respawnTime = 0L

    override fun tick() {
        super.tick()
        if (!level!!.isClientSide) {
            if (!phoenixIsAlive) {
                if (ticksExisted >= respawnTime) {
                    spawnFrostPhoenix()
                }
            }
        }
    }

    private fun spawnFrostPhoenix() {
        phoenixIsAlive = true
        val phoenix = FrostPhoenixEntity(level!!, blockPos)
        phoenix.setPos(blockPos.x + 0.5, blockPos.y + 1.0, blockPos.z + 0.5)
        level!!.addFreshEntity(phoenix)
    }

    fun reportPhoenixGone() {
        phoenixIsAlive = false
        respawnTime = ticksExisted + RESPAWN_COOLDOWN_TICKS
    }

    override fun load(blockState: BlockState, compound: CompoundNBT) {
        super.load(blockState, compound)
        phoenixIsAlive = compound.getBoolean(NBT_PHOENIX_IS_ALIVE)
        respawnTime = compound.getLong(NBT_RESPAWN_TIME)
    }

    override fun save(compound: CompoundNBT): CompoundNBT {
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
    }
}