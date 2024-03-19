package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellFreezeDataPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

/**
 * Default implementation of the AOTD spell freeze data class
 *
 * @property freezeTicks The number of freeze ticks remaining
 * @property freezePosition The position the player is frozen at
 * @property freezePitch The yaw direction the player is facing when frozen
 * @property freezeYaw The pitch direction the player is facing when frozen
 */
class PlayerSpellFreezeData : IPlayerSpellFreezeData {
    override var freezeTicks = 0
    override var freezePosition: Vec3? = null
    override var freezePitch: Float = 0f
    override var freezeYaw: Float = 0f

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private fun isServerSide(entityPlayer: Player): Boolean {
        return !entityPlayer.level.isClientSide
    }

    /**
     * Synchronizes freeze data between server and client
     *
     * @param entityPlayer The player to sync freeze data to
     */
    override fun sync(entityPlayer: Player) {
        // If we are on the server side sync this data to the client side
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                SpellFreezeDataPacket(freezeTicks, freezePosition, freezeYaw, freezePitch),
                entityPlayer as ServerPlayer
            )
        }
    }
}