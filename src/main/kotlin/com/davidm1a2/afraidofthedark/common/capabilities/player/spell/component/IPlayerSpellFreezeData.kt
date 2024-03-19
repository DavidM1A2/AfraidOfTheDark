package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

/**
 * An interface that stores data that lets us freeze the player
 *
 * @property freezeTicks The number of ticks the player should be frozen
 * @property freezePosition The position to freeze the player at
 */
interface IPlayerSpellFreezeData {
    var freezeTicks: Int
    var freezePosition: Vec3?
    var freezePitch: Float
    var freezeYaw: Float

    /**
     * Synchronizes freeze data between server and client
     *
     * @param entityPlayer The player to sync freeze data to
     */
    fun sync(entityPlayer: Player)
}