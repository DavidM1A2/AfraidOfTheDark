package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.RespawnPosition
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.dimension.IslandUtility
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.RegistryKey
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Handles that pertain to the void chest dimension
 */
class VoidChestHandler {
    /**
     * When we want to travel to the
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    fun onPreEntityTravelToDimension(event: EntityTravelToDimensionEvent) {
        // Server side processing only
        if (!event.entity.level.isClientSide) {
            // Get to and from dimension
            val fromDimension = event.entity.level.dimension()
            val toDimension = event.dimension

            // Test if the entity is a player, if so process it
            if (event.entity is ServerPlayerEntity) {
                val entityPlayer = event.entity as ServerPlayerEntity
                // Process the pre-teleport server side, if it returns true then we cancel the TP
                if (processPreTeleport(entityPlayer, fromDimension, toDimension)) {
                    event.isCanceled = true
                }
            }
        }
    }

    /**
     * Called right before the player is about to teleport
     *
     * @param entityPlayer  The teleporting player
     * @param dimensionFrom The dimension the player is currently in
     * @param dimensionTo   The dimension the player is going to
     * @return True to cancel the teleport, false otherwise
     */
    private fun processPreTeleport(entityPlayer: ServerPlayerEntity, dimensionFrom: RegistryKey<World>, dimensionTo: RegistryKey<World>): Boolean {
        // If we're going to dimension VOID_CHEST then we need to do some preprocesing and tests to ensure the player can continue
        if (dimensionTo == ModDimensions.VOID_CHEST_WORLD) {
            // We can't go from void chest to void chest
            if (dimensionFrom == ModDimensions.VOID_CHEST_WORLD) {
                return true
            }

            // Any other dimension is valid. We can go from any dimension other than the void_chest to the void_chest
            // We need to store off player position data pre-teleport

            val playerVoidChestData = entityPlayer.getVoidChestData()
            // Test for a valid spot within ~6 blocks of the player's position. This is used to ensure players do not come back to the overworld and straight into a
            // new portal block. This ensure you don't get stuck in a teleport loop
            // First just test the player's current position, if it's invalid search in a +/- 6 block radius in all directions for a valid position
            if (IslandUtility.isValidSpawnLocation(entityPlayer.level, entityPlayer.blockPosition())) {
                playerVoidChestData.preTeleportPosition = entityPlayer.blockPosition()
            } else {
                val preTeleportPosition = IslandUtility.findValidSpawnLocation(
                    entityPlayer.level,
                    entityPlayer.blockPosition(),
                    VALID_SPAWN_SEARCH_DISTANCE
                )
                // If we didn't find a valid spot around the player's position then throw an error and reject the teleport
                if (preTeleportPosition == null) {
                    entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.dimension.void_chest.no_spawn"))
                    return true
                } else {
                    playerVoidChestData.preTeleportPosition = preTeleportPosition
                }
            }
            // Set our pre-teleport dimension ID
            playerVoidChestData.preTeleportDimension = dimensionFrom

            // Write our player's respawn position to NBT
            playerVoidChestData.preTeleportRespawnPosition = RespawnPosition(
                entityPlayer.respawnPosition,
                entityPlayer.respawnDimension,
                entityPlayer.respawnAngle,
                entityPlayer.isRespawnForced
            )
        }
        return false
    }

    /**
     * Called when any player is finished changing dimensions
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    fun onPostEntityTravelToDimension(event: PlayerEvent.PlayerChangedDimensionEvent) {
        // Server side processing only
        if (!event.player.level.isClientSide) {
            // Get to and from dimension
            val fromDimension = event.from
            val toDimension = event.to

            // Get the player teleporting
            val entityPlayer = event.player as ServerPlayerEntity
            // Process the post-teleport server side
            processPostTeleport(entityPlayer, fromDimension, toDimension)
        }
    }

    /**
     * Called right after the player teleported
     *
     * @param entityPlayer  The teleporting player
     * @param dimensionFrom The dimension the player was in
     * @param dimensionTo   The dimension the player is now in
     */
    private fun processPostTeleport(entityPlayer: ServerPlayerEntity, dimensionFrom: RegistryKey<World>, dimensionTo: RegistryKey<World>) {
        // If the player entered the void chest dimension then set their position
        if (dimensionTo == ModDimensions.VOID_CHEST_WORLD) {
            // Grab the player's void chest data
            val playerVoidChestData = entityPlayer.getVoidChestData()
            // If the player was traveling to a friend's void chest grab that index, otherwise grab our own index
            // If the friend's index is -1 then we go to our position, otherwise go to the friends position
            val indexToGoTo = if (playerVoidChestData.friendsIndex == -1) {
                // Get or compute the player's index to go to based on who the furthest out player is
                IslandUtility.getOrAssignPlayerPositionalIndex(
                    entityPlayer.server!!.getLevel(ModDimensions.VOID_CHEST_WORLD)!!,
                    playerVoidChestData
                )
            } else {
                playerVoidChestData.friendsIndex
            }

            // Compute the player's X position based on the index
            val playerXBase = indexToGoTo * Constants.DISTANCE_BETWEEN_ISLANDS
            // Set the player's position and rotation for some reason we have to use the connection object to send a packet instead of just using entityplayer#setPosition
            entityPlayer.connection.teleport(playerXBase + 24.5, 104.0, 3.0, 0f, 0f)

            // Ensure the player respawns in the void chest if they die there
            entityPlayer.setRespawnPosition(
                ModDimensions.VOID_CHEST_WORLD,
                entityPlayer.blockPosition(),
                0f,
                true,
                false
            )
        }
        // If the player left the void chest reset their position
        if (dimensionFrom == ModDimensions.VOID_CHEST_WORLD) {
            // Grab the player's pre-teleport position
            val preTeleportPosition = entityPlayer.getVoidChestData().preTeleportPosition!!
            // Reset the player's position
            entityPlayer.connection.teleport(
                preTeleportPosition.x + 0.5,
                preTeleportPosition.y + 1.5,
                preTeleportPosition.z + 0.5,
                0f,
                0f
            )

            // Reset the player's respawn point to the original
            val respawnPosition = entityPlayer.getVoidChestData().preTeleportRespawnPosition!!
            entityPlayer.setRespawnPosition(
                respawnPosition.respawnDimension,
                respawnPosition.respawnPosition,
                respawnPosition.respawnAngle,
                respawnPosition.respawnForced,
                false
            )
        }
    }

    companion object {
        // Constant number of blocks to search for a spawn position
        private const val VALID_SPAWN_SEARCH_DISTANCE = 6
    }
}