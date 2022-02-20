package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.util.RegistryKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

class PlayerVoidChestData : IPlayerVoidChestData {
    override var positionalIndex = -1
    override var friendsIndex = -1
    override var preTeleportPosition: BlockPos? = null
    override var preTeleportRespawnPosition: RespawnPosition? = null
        set(value) {
            // We can't have a pre-teleport respawn dimension in the void chest, otherwise awful things
            // happen. Log an error if this ever happens
            field = if (value?.respawnDimension == ModDimensions.VOID_CHEST_WORLD) {
                LOG.error("Tried to set a player's void chest pre-teleport respawn position to the void chest: {}", value)
                value.copy(respawnDimension = World.OVERWORLD, respawnPosition = null)
            } else {
                value
            }
        }
    override var preTeleportDimension: RegistryKey<World>? = null
        set(value) {
            // We can't have a pre-teleport dimension in the void chest, otherwise awful things
            // happen. Log an error if this ever happens
            field = if (value == ModDimensions.VOID_CHEST_WORLD) {
                LOG.error("Tried to set a player's void chest pre-teleport position to the void chest: {}", value)
                World.OVERWORLD
            } else {
                value
            }
        }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}