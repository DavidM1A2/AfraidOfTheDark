package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getWardedBlockMap
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.spell.component.effect.WardSpellEffect
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimerWrapper
import net.minecraft.world.chunk.ChunkStatus
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.event.world.ChunkWatchEvent
import net.minecraftforge.event.world.ExplosionEvent
import net.minecraftforge.event.world.PistonEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import kotlin.random.Random

class SpellWardHandler {
    private val chunksToSync = mutableMapOf<PlayerEntity, DelayedWardSyncEntry>()

    // Events related to break speed

    @SubscribeEvent
    fun onBreakSpeed(event: PlayerEvent.BreakSpeed) {
        val strength = getWardStrength(event.entity.level, event.pos)
        if (strength != null) {
            val speedMultiplier = 1f - strength / WardSpellEffect.MAX_STRENGTH.toFloat()
            event.newSpeed = event.originalSpeed * MathHelper.lerp(speedMultiplier, LOWEST_MINING_SPEED, 1.0f)
        }
    }

    @SubscribeEvent
    fun onBlockBrokenEvent(event: BlockEvent.BreakEvent) {
        val world = event.world
        if (!world.isClientSide && world is World) {
            val blockPos = event.pos
            val chunkPos = ChunkPos(blockPos.x shr 4, blockPos.z shr 4)
            val chunk = world.getChunk(chunkPos.x, chunkPos.z)
            val wardedBlockMap = chunk.getWardedBlockMap()
            val wardStrength = wardedBlockMap.getWardStrength(blockPos)
            if (wardStrength != null) {
                wardedBlockMap.wardBlock(blockPos, null)
                wardedBlockMap.sync(world, chunkPos, blockPos = blockPos)
            }
        }
    }

    @SubscribeEvent
    fun onPistonPreEvent(event: PistonEvent.Pre) {
        val world = event.world
        if (!world.isClientSide && world is World) {
            val blockPos = event.pos

            // Check if the piston arm is warded
            if (event.pistonMoveType == PistonEvent.PistonMoveType.RETRACT) {
                val wardStrength = getWardStrength(world, blockPos.relative(event.direction, 1))
                if (wardStrength != null && wardStrength > 0) {
                    event.isCanceled = true
                    return
                }
            }

            // Retracting a normal piston is fine if the arm isn't warded
            val isStickyPiston = world.getBlockState(blockPos).block == Blocks.STICKY_PISTON
            if (!isStickyPiston && event.pistonMoveType == PistonEvent.PistonMoveType.RETRACT) {
                return
            }

            val pistonHelper = event.structureHelper
            if (pistonHelper == null || !pistonHelper.resolve()) {
                return
            }

            val blocksToChange = (pistonHelper.toDestroy + pistonHelper.toPush).toSet()
            for (blockToChange in blocksToChange) {
                val wardStrength = getWardStrength(world, blockToChange)
                // Block is warded, so cancel the piston movement
                if (wardStrength != null && wardStrength > 0) {
                    event.isCanceled = true
                    return
                }
            }
        }
    }

    @SubscribeEvent
    fun onNeighborNotifyEvent(event: BlockEvent.NeighborNotifyEvent) {
        val world = event.world
        if (!world.isClientSide && world is World) {
            val blockPos = event.pos
            val blockState = world.getBlockState(blockPos)
            if (blockState.isAir) {
                val chunkPos = ChunkPos(blockPos.x shr 4, blockPos.z shr 4)
                val chunk = world.getChunk(chunkPos.x, chunkPos.z)
                val wardedBlockMap = chunk.getWardedBlockMap()
                if (wardedBlockMap.getWardStrength(blockPos) != null) {
                    wardedBlockMap.wardBlock(blockPos, null)
                    wardedBlockMap.sync(world, chunkPos, blockPos = blockPos)
                }
            }
        }
    }

    @SubscribeEvent
    fun onExplosionEvent(event: ExplosionEvent.Detonate) {
        val world = event.world
        if (!world.isClientSide) {
            val blocks = event.affectedBlocks
            blocks.removeIf {
                val wardStrength = getWardStrength(world, it)
                if (wardStrength == null) {
                    false
                } else {
                    Random.nextDouble() <= (wardStrength.toDouble() / WardSpellEffect.MAX_STRENGTH)
                }
            }
        }
    }

    private fun getWardStrength(world: World, blockPos: BlockPos): Byte? {
        val chunk = world.getChunk(blockPos.x shr 4, blockPos.z shr 4)
        val wardedBlockMap = chunk.getWardedBlockMap()
        return wardedBlockMap.getWardStrength(blockPos)
    }

    // Events related to ward server <-> client synchronization

    @SubscribeEvent
    fun onChunkWatchEvent(event: ChunkWatchEvent.Watch) {
        chunksToSync.computeIfAbsent(event.player) {
            DelayedWardSyncEntry(event.world, event.player)
        }.chunkPositions.add(event.pos)
    }

    @SubscribeEvent
    fun onChunkUnWatchEvent(event: ChunkWatchEvent.UnWatch) {
        val chunkPositions = chunksToSync[event.player]?.chunkPositions
        if (chunkPositions != null) {
            chunkPositions.remove(event.pos)
            if (chunkPositions.isEmpty()) {
                chunksToSync.remove(event.player)
            }
        }
    }

    @SubscribeEvent
    fun onServerTickEvent(event: TickEvent.ServerTickEvent) {
        if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            chunksToSync.entries.removeIf { (_, value) ->
                with(value) {
                    ticksUntilNextTry = ticksUntilNextTry - 1
                    if (ticksUntilNextTry <= 0) {
                        ticksUntilNextTry = TICKS_PER_WARD_SYNC_ATTEMPT
                        chunkPositions.removeIf {
                            trySyncingWardedBlocks(world, player, it)
                        }
                        chunkPositions.isEmpty()
                    } else {
                        false
                    }
                }
            }
        }
    }

    private fun trySyncingWardedBlocks(world: World, player: PlayerEntity, chunkPos: ChunkPos): Boolean {
        val wardedBlockMap = when (val chunk = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false)) {
            is Chunk -> chunk.getWardedBlockMap()
            is ChunkPrimerWrapper -> chunk.wrapped.getCapability(ModCapabilities.WARDED_BLOCK_MAP).resolve().orElse(null)
            else -> null
        }

        return if (wardedBlockMap == null) {
            false
        } else {
            wardedBlockMap.sync(world, chunkPos, player as ServerPlayerEntity)
            true
        }
    }

    private data class DelayedWardSyncEntry(
        val world: World,
        val player: PlayerEntity,
        val chunkPositions: MutableSet<ChunkPos> = mutableSetOf(),
        var ticksUntilNextTry: Int = TICKS_PER_WARD_SYNC_ATTEMPT / 2
    )

    companion object {
        private const val LOWEST_MINING_SPEED = 0.01f
        private const val TICKS_PER_WARD_SYNC_ATTEMPT = 60
    }
}