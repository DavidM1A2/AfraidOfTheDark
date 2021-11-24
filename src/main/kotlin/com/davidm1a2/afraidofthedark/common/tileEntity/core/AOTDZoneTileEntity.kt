package com.davidm1a2.afraidofthedark.common.tileEntity.core

import com.davidm1a2.afraidofthedark.common.event.custom.PlayerEnterZoneEvent
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerExitZoneEvent
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.Constants
import java.util.UUID

/**
 * Base class for all tile entities that require ticking every game tick
 *
 * @constructor initializes the tile entity fields
 * @param tileEntityType The tile entity's type
 */
abstract class AOTDZoneTileEntity(tileEntityType: TileEntityType<*>) : AOTDTickingTileEntity(tileEntityType), ITickableTileEntity {
    private val playersInZone = mutableSetOf<UUID>()
    protected abstract val zone: AxisAlignedBB

    /**
     * Called every tick to update the tile entity's state
     */
    override fun tick() {
        super.tick()
        // Server side processing only
        if (level?.isClientSide == false) {
            updateNearbyPlayers()
        }
    }

    private fun updateNearbyPlayers() {
        // If we've existed for a multiple of 40 ticks perform a check for nearby players
        if (ticksExisted % TICKS_BETWEEN_PLAYER_CHECKS == 0L) {
            // Grab all nearby players
            val nearbyPlayers = level!!.getEntitiesOfClass(PlayerEntity::class.java, zone)
            for (entityPlayer in nearbyPlayers) {
                if (!playersInZone.contains(entityPlayer.uuid)) {
                    playersInZone.add(entityPlayer.uuid)
                    MinecraftForge.EVENT_BUS.post(PlayerEnterZoneEvent(entityPlayer, this.type))
                }
                playerInZone(entityPlayer)
            }
            val removePlayers = mutableSetOf<UUID>()
            for (player in playersInZone) {
                if (nearbyPlayers.none { it.uuid == player }) {
                    removePlayers.add(player)
                    val entityPlayer = level!!.getPlayerByUUID(player)
                    if (entityPlayer != null) { // Only post event if player exists in the world
                        MinecraftForge.EVENT_BUS.post(PlayerExitZoneEvent(entityPlayer, this.type))
                    }
                }
            }
            removePlayers.forEach { playersInZone.remove(it) }
        }
    }

    // NOT abstract, because a subclass shouldn't be forced to override it
    protected open fun playerInZone(player: PlayerEntity) {}

    override fun load(blockState: BlockState, compound: CompoundNBT) {
        super.load(blockState, compound)
        compound.getList(NBT_PLAYERS, Constants.NBT.TAG_INT_ARRAY).forEach {
            playersInZone.add(NBTUtil.loadUUID(it))
        }
    }

    override fun save(compound: CompoundNBT): CompoundNBT {
        super.save(compound)
        compound.put(NBT_PLAYERS, ListNBT().apply { playersInZone.forEach { add(NBTUtil.createUUID(it)) } })
        return compound
    }

    companion object {
        private const val NBT_PLAYERS = "players"
        private const val TICKS_BETWEEN_PLAYER_CHECKS = 40
    }
}