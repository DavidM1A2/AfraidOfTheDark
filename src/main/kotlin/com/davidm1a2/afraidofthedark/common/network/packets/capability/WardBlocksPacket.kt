package com.davidm1a2.afraidofthedark.common.network.packets.capability

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos

class WardBlocksPacket(
    internal val chunkPos: ChunkPos,
    internal val wardedBlockMap: List<Pair<BlockPos, Byte?>>
)