package com.davidm1a2.afraidofthedark.common.capabilities.chunk.ward

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World

interface IWardedBlockMap {
    /**
     * @return the list of warded blocks in the given chunks
     */
    fun getWardedBlocks(): List<BlockPos>

    /**
     * Gets the strength of the ward on a given block
     *
     * @param blockPos The block position to get the ward strength for
     * @return The ward strength, or null if the block isn't warded
     */
    fun getWardStrength(blockPos: BlockPos): Int?

    /**
     * Wards a given block with a given strength, or clears the ward if strength is null
     *
     * @param blockPos The block position to ward
     * @param strength The strength to ward the position at, or null to clear it
     */
    fun wardBlock(blockPos: BlockPos, strength: Int? = null)

    /**
     * Syncs a given warded block with the client. Can also sync all warded blocks if null is specified
     *
     * @param world The world to sync
     * @param chunkPos The chunk to sync
     * @param playerEntity The player to sync the data to. If null, all players watching the chunk are sent the data
     * @param blockPos An optional blockPos to sync the warded strength for. If omitted the entire map is synced
     */
    fun sync(world: World, chunkPos: ChunkPos, playerEntity: PlayerEntity? = null, blockPos: BlockPos? = null)
}