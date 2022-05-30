package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getWardedBlockMap
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * This is a packet that is sent from server to a client that tells the client what blocks are warded in a chunk
 */
class WardBlocksPacketProcessor : PacketProcessor<WardBlocksPacket> {
    override fun encode(msg: WardBlocksPacket, buf: PacketBuffer) {
        buf.writeInt(msg.chunkPos.x)
        buf.writeInt(msg.chunkPos.z)

        val wardedBlocks = msg.wardedBlockMap.filter { it.second != null }
        val unwardedBlocks = msg.wardedBlockMap.filter { it.second == null }
        val wardedBlockCount = wardedBlocks.size
        val unwardedBlockCount = unwardedBlocks.size

        buf.writeInt(wardedBlockCount)
        buf.writeLongArray(wardedBlocks.map { it.first.asLong() }.toLongArray())
        buf.writeByteArray(wardedBlocks.map { it.second!!.toByte() }.toByteArray())

        buf.writeInt(unwardedBlockCount)
        buf.writeLongArray(unwardedBlocks.map { it.first.asLong() }.toLongArray())
    }

    override fun decode(buf: PacketBuffer): WardBlocksPacket {
        val chunkPos = ChunkPos(buf.readInt(), buf.readInt())

        val wardedBlockCount = buf.readInt()
        val rawWardedKeys = LongArray(wardedBlockCount)
        buf.readLongArray(rawWardedKeys)
        val wardedKeys = rawWardedKeys.map { BlockPos.of(it) }
        val wardedValues = buf.readByteArray(wardedBlockCount).toTypedArray().map { it.toInt() }

        val unwardedBlockCount = buf.readInt()
        val rawUnwardedKeys = LongArray(unwardedBlockCount)
        buf.readLongArray(rawUnwardedKeys)
        val unwardedKeys = rawUnwardedKeys.map { BlockPos.of(it) }

        val wardedBlockMap = wardedKeys.zip(wardedValues).toMutableList<Pair<BlockPos, Int?>>()
        wardedBlockMap.addAll(unwardedKeys.map { it to null })

        return WardBlocksPacket(chunkPos, wardedBlockMap)
    }

    override fun process(msg: WardBlocksPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val world = Minecraft.getInstance().level
            val chunkPos = msg.chunkPos
            val wardedBlockMapToAdd = msg.wardedBlockMap
            if (world != null) {
                val chunk = world.getChunk(chunkPos.x, chunkPos.z)
                val wardedBlockMap = chunk.getWardedBlockMap()
                wardedBlockMapToAdd.forEach { wardedBlockMap.wardBlock(it.first, it.second) }
            }
        }
    }
}