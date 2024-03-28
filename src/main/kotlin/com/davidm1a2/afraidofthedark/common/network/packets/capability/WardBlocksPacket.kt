package com.davidm1a2.afraidofthedark.common.network.packets.capability

import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos

class WardBlocksPacket(
    internal val chunkPos: ChunkPos,
    internal val wardedBlockMap: List<Pair<BlockPos, Int?>>
)