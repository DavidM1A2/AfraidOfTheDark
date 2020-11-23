package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Vec3d

/**
 * An interface that stores data that lets us freeze the player
 *
 * @property freezeTicks The number of ticks the player should be frozen
 * @property freezePosition The position to freeze the player at
 */
interface IAOTDPlayerSpellFreezeData {
    var freezeTicks: Int
    var freezePosition: Vec3d?

    /**
     * Sets the direction the player was looking when frozen
     *
     * @param yaw The yaw of the direction the player is looking
     * @param pitch The pitch of the direction the player is looking
     */
    fun setFreezeDirection(yaw: Float, pitch: Float)

    /**
     * Gets the yaw of the direction the player is frozen towards
     *
     * @return The yaw that the player was looking when frozen
     */
    fun getFreezeYaw(): Float

    /**
     * Gets the pitch of the direction the player is frozen towards
     *
     * @return The pitch that the player was looking when frozen
     */
    fun getFreezePitch(): Float

    /**
     * Synchronizes freeze data between server and client
     *
     * @param entityPlayer The player to sync freeze data to
     */
    fun sync(entityPlayer: PlayerEntity)
}