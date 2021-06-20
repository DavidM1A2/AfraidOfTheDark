package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.FreezeDataPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

/**
 * Default implementation of the AOTD spell freeze data class
 *
 * @property freezeTicks The number of freeze ticks remaining
 * @property freezePosition The position the player is frozen at
 * @property yaw The yaw direction the player is facing when frozen
 * @property pitch The pitch direction the player is facing when frozen
 */
class PlayerSpellFreezeData : IPlayerSpellFreezeData {
    override var freezeTicks = 0
    override var freezePosition: Vec3d? = null
    private var yaw = 0f
    private var pitch = 0f

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private fun isServerSide(entityPlayer: PlayerEntity): Boolean {
        return !entityPlayer.world.isRemote
    }

    /**
     * Sets the direction the player was looking when frozen
     *
     * @param yaw The yaw of the direction the player is looking
     * @param pitch The pitch of the direction the player is looking
     */
    override fun setFreezeDirection(yaw: Float, pitch: Float) {
        this.yaw = yaw
        this.pitch = pitch
    }

    /**
     * Gets the yaw of the direction the player is frozen towards
     *
     * @return The yaw that the player was looking when frozen
     */
    override fun getFreezeYaw(): Float {
        return yaw
    }

    /**
     * Gets the pitch of the direction the player is frozen towards
     *
     * @return The pitch that the player was looking when frozen
     */
    override fun getFreezePitch(): Float {
        return pitch
    }

    /**
     * Synchronizes freeze data between server and client
     *
     * @param entityPlayer The player to sync freeze data to
     */
    override fun sync(entityPlayer: PlayerEntity) {
        // If we are on the server side sync this data to the client side
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                FreezeDataPacket(freezeTicks, freezePosition, yaw, pitch),
                entityPlayer as ServerPlayerEntity
            )
        }
    }
}