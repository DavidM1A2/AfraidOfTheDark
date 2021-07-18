package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.nbt.INBT
import net.minecraft.nbt.IntNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraft.util.math.ChunkPos
import net.minecraftforge.common.capabilities.Capability
import org.apache.logging.log4j.LogManager

class WorldMasterChunkMapStorage : Capability.IStorage<IWorldMasterChunkMap> {
    override fun writeNBT(capability: Capability<IWorldMasterChunkMap>, instance: IWorldMasterChunkMap, side: Direction?): INBT {
        val masterChunksNbt = ListNBT()
        instance.getMasterChunks().forEach {
            masterChunksNbt.add(IntNBT.valueOf(it.x))
            masterChunksNbt.add(IntNBT.valueOf(it.z))
        }

        return masterChunksNbt
    }

    override fun readNBT(capability: Capability<IWorldMasterChunkMap>, instance: IWorldMasterChunkMap, side: Direction?, nbt: INBT) {
        val masterChunks = mutableListOf<ChunkPos>()

        if (nbt is ListNBT) {
            for (i in 0 until nbt.size step 2) {
                masterChunks.add(ChunkPos(nbt.getInt(i), nbt.getInt(i + 1)))
            }
            instance.setMasterChunks(masterChunks)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an ListNBT")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}