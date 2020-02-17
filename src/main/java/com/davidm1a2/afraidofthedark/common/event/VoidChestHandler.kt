package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getVoidChestData
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.dimension.IslandUtility
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent

/**
 * Handles that pertain to the void chest dimension
 */
class VoidChestHandler
{
    /**
     * Called when the player dies, set them back to their void chest home
     *
     * @param event The data of the respawned player
     */
    @SubscribeEvent
    fun onPlayerRespawnEvent(event: PlayerRespawnEvent)
    {
        // Server side processing only
        if (!event.player.world.isRemote)
        {
            if (event.player.dimension == ModDimensions.VOID_CHEST.id)
            {
                val playerVoidChestData = event.player.getVoidChestData()

                // If the player was traveling to a friend's void chest grab that index, otherwise grab our own index
                // If the friend's index is -1 then we go to our position, otherwise go to the friends position and wipe out the friends index variable
                val indexToGoTo = if (playerVoidChestData.friendsIndex == -1)
                {
                    // Get or compute the player's index to go to based on who the furthest out player is
                    IslandUtility.getOrAssignPlayerPositionalIndex(event.player.server!!.getWorld(ModDimensions.VOID_CHEST.id), playerVoidChestData)
                }
                else
                {
                    playerVoidChestData.friendsIndex
                }

                // Compute the player's X position based on the index
                val playerXBase = indexToGoTo * AfraidOfTheDark.INSTANCE.configurationHandler.blocksBetweenIslands
                (event.player as EntityPlayerMP).connection.setPlayerLocation(playerXBase + 24.5, 104.0, 3.0, 0f, 0f)
            }
        }
    }

    /**
     * When we want to travel to the
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    fun onPreEntityTravelToDimension(event: EntityTravelToDimensionEvent)
    {
        // Server side processing only
        if (!event.entity.world.isRemote)
        {
            // Get to and from dimension
            val fromDimension = event.entity.dimension
            val toDimension = event.dimension

            // Test if the entity is a player, if so process it
            if (event.entity is EntityPlayerMP)
            {
                val entityPlayer = event.entity as EntityPlayerMP
                // Process the pre-teleport server side, if it returns true then we cancel the TP
                if (processPreTeleport(entityPlayer, fromDimension, toDimension))
                {
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
    private fun processPreTeleport(entityPlayer: EntityPlayerMP, dimensionFrom: Int, dimensionTo: Int): Boolean
    {
        // If we're going to dimension VOID_CHEST then we need to do some preprocesing and tests to ensure the player can continue
        if (dimensionTo == ModDimensions.VOID_CHEST.id)
        {
            // We can't go from void chest to void chest
            if (dimensionFrom == ModDimensions.VOID_CHEST.id)
            {
                return true
            }

            // Any other dimension is valid. We can go from any dimension other than the void_chest to the void_chest
            // We need to store off player position data pre-teleport

            val playerVoidChestData = entityPlayer.getVoidChestData()
            // Test for a valid spot within ~6 blocks of the player's position. This is used to ensure players do not come back to the overworld and straight into a
            // new portal block. This ensure you don't get stuck in a teleport loop
            // First just test the player's current position, if it's invalid search in a +/- 6 block radius in all directions for a valid position
            if (IslandUtility.isValidSpawnLocation(entityPlayer.world, entityPlayer.position))
            {
                playerVoidChestData.preTeleportPosition = entityPlayer.position
            }
            else
            {
                val preTeleportPosition = IslandUtility.findValidSpawnLocation(entityPlayer.world, entityPlayer.position, VALID_SPAWN_SEARCH_DISTANCE)
                // If we didn't find a valid spot around the player's position then throw an error and reject the teleport
                if (preTeleportPosition == null)
                {
                    entityPlayer.sendMessage(TextComponentTranslation("message.afraidofthedark:dimension.void_chest.no_spawn"))
                    return true
                }
                else
                {
                    playerVoidChestData.preTeleportPosition = preTeleportPosition
                }
            }
            // Set our pre-teleport dimension ID
            playerVoidChestData.preTeleportDimensionID = dimensionFrom
        }
        return false
    }

    /**
     * Called when any player is finished changing dimensions
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    fun onPostEntityTravelToDimension(event: PlayerChangedDimensionEvent)
    {
        // Server side processing only
        if (!event.player.world.isRemote)
        {
            // Get to and from dimension
            val fromDimension = event.fromDim
            val toDimension = event.toDim

            // Get the player teleporting
            val entityPlayer = event.player as EntityPlayerMP
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
    private fun processPostTeleport(entityPlayer: EntityPlayerMP, dimensionFrom: Int, dimensionTo: Int)
    {
        // If the player entered the void chest dimension then set their position
        if (dimensionTo == ModDimensions.VOID_CHEST.id)
        {
            // Grab the player's void chest data
            val playerVoidChestData = entityPlayer.getVoidChestData()
            // If the player was traveling to a friend's void chest grab that index, otherwise grab our own index
            // If the friend's index is -1 then we go to our position, otherwise go to the friends position
            val indexToGoTo = if (playerVoidChestData.friendsIndex == -1) // Get or compute the player's index to go to based on who the furthest out player is
            {
                IslandUtility.getOrAssignPlayerPositionalIndex(entityPlayer.server!!.getWorld(ModDimensions.VOID_CHEST.id), playerVoidChestData)
            }
            else
            {
                playerVoidChestData.friendsIndex
            }

            // Compute the player's X position based on the index
            val playerXBase = indexToGoTo * AfraidOfTheDark.INSTANCE.configurationHandler.blocksBetweenIslands
            // Set the player's position and rotation for some reason we have to use the connection object to send a packet instead of just using entityplayer#setPosition
            entityPlayer.connection.setPlayerLocation(playerXBase + 24.5, 104.0, 3.0, 0f, 0f)
        }
        // If the player left the void chest reset their position
        if (dimensionFrom == ModDimensions.VOID_CHEST.id)
        {
            // Grab the player's pre-teleport position
            val preTeleportPosition = entityPlayer.getVoidChestData().preTeleportPosition
            // Reset the player's position
            entityPlayer.connection.setPlayerLocation(preTeleportPosition!!.x + 0.5, preTeleportPosition.y + 1.5, preTeleportPosition.z + 0.5, 0f, 0f)
        }
    }

    companion object
    {
        // Constant number of blocks to search for a spawn position
        private const val VALID_SPAWN_SEARCH_DISTANCE = 6
    }
}