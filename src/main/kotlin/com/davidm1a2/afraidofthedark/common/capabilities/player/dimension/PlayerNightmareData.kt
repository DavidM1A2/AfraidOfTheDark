package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.core.BlockPos
import net.minecraft.nbt.ListTag
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import org.apache.logging.log4j.LogManager

class PlayerNightmareData : IPlayerNightmareData {
    override var positionalIndex = -1
    override var preTeleportPlayerInventory: ListTag? = null
    override var preTeleportPosition: BlockPos? = null
    override var preTeleportRespawnPosition: RespawnPosition? = null
        set(value) {
            // We can't have a pre-teleport respawn dimension in the nightmare, otherwise awful things
            // happen. Log an error if this ever happens
            field = if (value?.respawnDimension == ModDimensions.NIGHTMARE_WORLD) {
                LOG.error("Tried to set a player's nightmare pre-teleport respawn position to the nightmare: {}", value)
                value?.copy(respawnDimension = Level.OVERWORLD, respawnPosition = null)
            } else {
                value
            }
        }
    override var preTeleportDimension: ResourceKey<Level>? = null
        set(value) {
            // We can't have a pre-teleport dimension in the nightmare, otherwise awful things
            // happen. Log an error if this ever happens
            field = if (value == ModDimensions.NIGHTMARE_WORLD) {
                LOG.error("Tried to set a player's nightmare pre-teleport position to the nightmare: {}", value)
                Level.OVERWORLD
            } else {
                value
            }
        }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}