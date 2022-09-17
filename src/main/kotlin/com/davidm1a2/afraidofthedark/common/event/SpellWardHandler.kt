package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getWardedBlockMap
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.vector.Vector3d
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
    fun onBreakSpeedEvent(event: PlayerEvent.BreakSpeed) {
        val blockPos = event.pos
        val speedReductionPercent = getWardStrength(event.entity.level, blockPos)?.let { getMiningSpeedReductionPercent(it) }
        val world = event.entity.level
        if (speedReductionPercent != null) {
            event.newSpeed = event.originalSpeed * (1f - speedReductionPercent)
            if (!world.isClientSide && event.entity.tickCount % 24 == 0) {
                spawnWardParticle(world, blockPos, Direction.values().toList())
            }
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
                val armPos = blockPos.relative(event.direction, 1)
                val wardStrength = getWardStrength(world, armPos)
                if (wardStrength != null) {
                    event.isCanceled = true
                    spawnWardParticle(world, armPos, Direction.values().toList())
                    return
                }
                val isStickyPiston = world.getBlockState(blockPos).block == Blocks.STICKY_PISTON
                // Retracting a normal piston is fine if the arm isn't warded
                if (isStickyPiston) {
                    val blockPosToPull = blockPos.relative(event.direction, 2)
                    val blockToPullStrength = getWardStrength(world, blockPosToPull)
                    if (blockToPullStrength != null) {
                        event.isCanceled = true
                        spawnWardParticle(world, blockPosToPull, Direction.values().toList())
                        return
                    }
                } else {
                    return
                }
            }

            val pistonHelper = event.structureHelper
            if (pistonHelper == null || !pistonHelper.resolve()) {
                return
            }

            val blocksToChange = (pistonHelper.toDestroy + pistonHelper.toPush).toSet()
            for (blockToChange in blocksToChange) {
                val wardStrength = getWardStrength(world, blockToChange)
                // Block is warded, so cancel the piston movement
                if (wardStrength != null) {
                    event.isCanceled = true
                    spawnWardParticle(world, blockToChange, Direction.values().toList())
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
                    spawnWardParticle(world, blockPos, Direction.values().toList())
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
                    val didNotExplode = Random.nextDouble() <= (1f - getExplodeChance(wardStrength))
                    if (didNotExplode) {
                        spawnWardParticle(world, it, Direction.values().toList())
                    }
                    didNotExplode
                }
            }
        }
    }

    private fun getWardStrength(world: World, blockPos: BlockPos): Int? {
        val chunk = world.getChunk(blockPos.x shr 4, blockPos.z shr 4)
        val wardedBlockMap = chunk.getWardedBlockMap()
        return wardedBlockMap.getWardStrength(blockPos)
    }

    private fun spawnWardParticle(world: World, blockPos: BlockPos, directions: List<Direction> = Direction.values().toList()) {
        val positions = directions.map {
            Vector3d(blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5)
                .add(it.stepX * 0.505, it.stepY * 0.505, it.stepZ * 0.505)
        }
        val speeds = directions.map {
            Vector3d(it.ordinal.toDouble(), 0.0, 0.0)
        }
        AfraidOfTheDark.packetHandler.sendToAllAround(
            ParticlePacket.builder().particle(ModParticles.WARD).positions(positions).speeds(speeds).build(),
            world.dimension(),
            blockPos.x + 0.5,
            blockPos.y + 0.5,
            blockPos.z + 0.5,
            50.0
        )
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

    private fun getExplodeChance(wardStrength: Int): Float {
        val powerOfTwo = 1 shl wardStrength
        return 1f / powerOfTwo
    }

    private fun getMiningSpeedReductionPercent(wardStrength: Int): Float {
        val powerOfTwo = 1 shl wardStrength
        return (powerOfTwo - 1f) / powerOfTwo
    }

    private data class DelayedWardSyncEntry(
        val world: World,
        val player: PlayerEntity,
        val chunkPositions: MutableSet<ChunkPos> = mutableSetOf(),
        var ticksUntilNextTry: Int = TICKS_PER_WARD_SYNC_ATTEMPT / 2
    )

    companion object {
        private const val TICKS_PER_WARD_SYNC_ATTEMPT = 60
    }
}