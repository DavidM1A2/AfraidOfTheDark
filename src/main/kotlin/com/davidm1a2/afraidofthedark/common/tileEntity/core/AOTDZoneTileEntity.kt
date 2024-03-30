package com.davidm1a2.afraidofthedark.common.tileEntity.core

import com.davidm1a2.afraidofthedark.common.event.custom.PlayerEnterZoneEvent
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerExitZoneEvent
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.Tag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraftforge.common.MinecraftForge
import java.util.*

/**
 * Base class for all tile entities that require ticking every game tick
 *
 * @constructor initializes the tile entity fields
 */
abstract class AOTDZoneTileEntity(type: BlockEntityType<*>, blockPos: BlockPos, blockState: BlockState) : AOTDTickingTileEntity(type, blockPos, blockState) {
    private val playersInZone = mutableSetOf<UUID>()
    protected abstract val zone: AABB

    // NOT abstract, because a subclass shouldn't be forced to override it
    protected open fun playerInZone(player: Player) {}

    override fun load(compound: CompoundTag) {
        super.load(compound)
        compound.getList(NBT_PLAYERS, Tag.TAG_INT_ARRAY.toInt()).forEach {
            playersInZone.add(NbtUtils.loadUUID(it))
        }
    }

    override fun save(compound: CompoundTag): CompoundTag {
        super.save(compound)
        compound.put(NBT_PLAYERS, ListTag().apply { playersInZone.forEach { add(NbtUtils.createUUID(it)) } })
        return compound
    }

    companion object {
        private const val NBT_PLAYERS = "players"
        private const val TICKS_BETWEEN_PLAYER_CHECKS = 40

        fun serverTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: AOTDZoneTileEntity) {
            baseTick(level, blockPos, blockState, blockEntity)
            val playersInZone = blockEntity.playersInZone
            // If we've existed for a multiple of 40 ticks perform a check for nearby players
            if (blockEntity.ticksExisted % TICKS_BETWEEN_PLAYER_CHECKS == 0L) {
                // Grab all nearby players
                val nearbyPlayers = level.getEntitiesOfClass(Player::class.java, blockEntity.zone)
                for (entityPlayer in nearbyPlayers) {
                    if (!playersInZone.contains(entityPlayer.uuid)) {
                        playersInZone.add(entityPlayer.uuid)
                        MinecraftForge.EVENT_BUS.post(PlayerEnterZoneEvent(entityPlayer, blockEntity.type))
                    }
                    blockEntity.playerInZone(entityPlayer)
                }
                val removePlayers = mutableSetOf<UUID>()
                for (player in playersInZone) {
                    if (nearbyPlayers.none { it.uuid == player }) {
                        removePlayers.add(player)
                        val entityPlayer = level.getPlayerByUUID(player)
                        if (entityPlayer != null) { // Only post event if player exists in the world
                            MinecraftForge.EVENT_BUS.post(PlayerExitZoneEvent(entityPlayer, blockEntity.type))
                        }
                    }
                }
                removePlayers.forEach { playersInZone.remove(it) }
            }
        }
    }
}