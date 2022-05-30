package com.davidm1a2.afraidofthedark.common.capabilities.chunk.ward

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getWardedBlockMap
import com.davidm1a2.afraidofthedark.common.network.packets.capability.WardBlocksPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

class WardedBlockMap : IWardedBlockMap {
    private val map = mutableMapOf<Long, Int?>()

    override fun getWardedBlocks(): List<BlockPos> {
        return map.keys.map { BlockPos.of(it) }
    }

    override fun getWardStrength(blockPos: BlockPos): Int? {
        val chunkAdjustedPos = BlockPos(blockPos.x and 15, blockPos.y, blockPos.z and 15)
        return map[chunkAdjustedPos.asLong()]
    }

    override fun wardBlock(blockPos: BlockPos, strength: Int?) {
        val chunkAdjustedPos = BlockPos(blockPos.x and 15, blockPos.y, blockPos.z and 15)
        map[chunkAdjustedPos.asLong()] = strength
    }

    override fun sync(world: World, chunkPos: ChunkPos, playerEntity: PlayerEntity?, blockPos: BlockPos?) {
        if (world.isClientSide) {
            throw IllegalStateException("Only a server can sync warded blocks to clients")
        }

        val chunk = world.getChunk(chunkPos.x, chunkPos.z)
        if (chunk.getWardedBlockMap() != this) {
            LOG.error("Attempted to sync the warded block map for $chunkPos which is not this warded block map")
            return
        }
        val positionsToSync = blockPos?.let { listOf(it) } ?: getWardedBlocks()
        val syncData = positionsToSync.associateWith { getWardStrength(it) }

        syncData.entries
            .map { it.key to it.value }
            // Send 100 at a time to avoid sending massive packets
            .chunked(100)
            .forEach {
                val packet = WardBlocksPacket(chunkPos, it)
                if (playerEntity == null) {
                    AfraidOfTheDark.packetHandler.sendToChunk(packet, chunk)
                } else {
                    AfraidOfTheDark.packetHandler.sendTo(packet, playerEntity as ServerPlayerEntity)
                }
            }

        // We can safely remove nulls from our map to save memory now that we've synced that info to clients
        syncData.filterValues { it == null }
            .forEach { (key, _) -> map.remove(key.asLong()) }
    }

    companion object {
        private val LOG = LogManager.getLogger(WardedBlockMap::class.java)
    }
}