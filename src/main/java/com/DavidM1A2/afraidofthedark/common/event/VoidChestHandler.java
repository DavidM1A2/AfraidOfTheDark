package com.DavidM1A2.afraidofthedark.common.event;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.DavidM1A2.afraidofthedark.common.dimension.IslandUtility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Handles that pertain to the void chest dimension
 */
public class VoidChestHandler
{
    // Constant number of blocks to search for a spawn position
    private static final int VALID_SPAWN_SEARCH_DISTANCE = 6;

    /**
     * Called when the player dies, set them back to their void chest home
     *
     * @param event The data of the respawned player
     */
    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event)
    {
        // Server side processing only
        if (!event.player.world.isRemote)
        {
            if (event.player.dimension == ModDimensions.VOID_CHEST.getId())
            {
                IAOTDPlayerVoidChestData playerVoidChestData = event.player.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null);
                // If the player was traveling to a friend's void chest grab that index, otherwise grab our own index
                int indexToGoTo;
                // If the friend's index is -1 then we go to our position, otherwise go to the friends position and wipe out the friends index variable
                if (playerVoidChestData.getFriendsIndex() == -1)
                // Get or compute the player's index to go to based on who the furthest out player is
                {
                    indexToGoTo = IslandUtility.getOrAssignPlayerPositionalIndex(event.player.getServer().getWorld(ModDimensions.VOID_CHEST.getId()), playerVoidChestData);
                }
                else
                {
                    indexToGoTo = playerVoidChestData.getFriendsIndex();
                }
                // Compute the player's X position based on the index
                int playerXBase = indexToGoTo * AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();
                ((EntityPlayerMP) event.player).connection.setPlayerLocation(playerXBase + 24.5, 104, 3, 0, 0);
            }
        }
    }

    /**
     * When we want to travel to the
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    public void onPreEntityTravelToDimension(EntityTravelToDimensionEvent event)
    {
        // Server side processing only
        if (!event.getEntity().world.isRemote)
        {
            // Get to and from dimension
            int fromDimension = event.getEntity().dimension;
            int toDimension = event.getDimension();
            // Test if the entity is a player, if so process it
            if (event.getEntity() instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityPlayer = (EntityPlayerMP) event.getEntity();
                // Process the pre-teleport server side, if it returns true then we cancel the TP
                if (this.processPreTeleport(entityPlayer, fromDimension, toDimension))
                {
                    event.setCanceled(true);
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
    private boolean processPreTeleport(EntityPlayerMP entityPlayer, int dimensionFrom, int dimensionTo)
    {
        // If we're going to dimension VOID_CHEST then we need to do some preprocesing and tests to ensure the player can continue
        if (dimensionTo == ModDimensions.VOID_CHEST.getId())
        {
            // We can't go from void chest to void chest
            if (dimensionFrom == ModDimensions.VOID_CHEST.getId())
            {
                return true;
            }

            // Any other dimension is valid. We can go from any dimension other than the void_chest to the void_chest
            // We need to store off player position data pre-teleport
            IAOTDPlayerVoidChestData playerVoidChestData = entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null);
            // Test for a valid spot within ~6 blocks of the player's position. This is used to ensure players do not come back to the overworld and straight into a
            // new portal block. This ensure you don't get stuck in a teleport loop
            // First just test the player's current position, if it's invalid search in a +/- 6 block radius in all directions for a valid position
            if (IslandUtility.isValidSpawnLocation(entityPlayer.world, entityPlayer.getPosition()))
            {
                playerVoidChestData.setPreTeleportPosition(entityPlayer.getPosition());
            }
            else
            {
                BlockPos preTeleportPosition = IslandUtility.findValidSpawnLocation(entityPlayer.world, entityPlayer.getPosition(), VALID_SPAWN_SEARCH_DISTANCE);
                // If we didn't find a valid spot around the player's position then throw an error and reject the teleport
                if (preTeleportPosition == null)
                {
                    entityPlayer.sendMessage(new TextComponentTranslation("aotd.dimension.void_chest.no_spawn"));
                    return true;
                }
                else
                {
                    playerVoidChestData.setPreTeleportPosition(preTeleportPosition);
                }
            }
            // Set our pre-teleport dimension ID
            playerVoidChestData.setPreTeleportDimensionID(dimensionFrom);
        }
        return false;
    }

    /**
     * Called when any player is finished changing dimensions
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    public void onPostEntityTravelToDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        // Server side processing only
        if (!event.player.world.isRemote)
        {
            // Get to and from dimension
            int fromDimension = event.fromDim;
            int toDimension = event.toDim;
            // Get the player teleporting
            EntityPlayerMP entityPlayer = (EntityPlayerMP) event.player;
            // Process the post-teleport server side
            this.processPostTeleport(entityPlayer, fromDimension, toDimension);
        }
    }

    /**
     * Called right after the player teleported
     *
     * @param entityPlayer  The teleporting player
     * @param dimensionFrom The dimension the player was in
     * @param dimensionTo   The dimension the player is now in
     */
    private void processPostTeleport(EntityPlayerMP entityPlayer, int dimensionFrom, int dimensionTo)
    {
        // If the player entered the void chest dimension then set their position
        if (dimensionTo == ModDimensions.VOID_CHEST.getId())
        {
            // Grab the player's void chest data
            IAOTDPlayerVoidChestData playerVoidChestData = entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null);
            // If the player was traveling to a friend's void chest grab that index, otherwise grab our own index
            int indexToGoTo;
            // If the friend's index is -1 then we go to our position, otherwise go to the friends position
            if (playerVoidChestData.getFriendsIndex() == -1)
            // Get or compute the player's index to go to based on who the furthest out player is
            {
                indexToGoTo = IslandUtility.getOrAssignPlayerPositionalIndex(entityPlayer.getServer().getWorld(ModDimensions.VOID_CHEST.getId()), playerVoidChestData);
            }
            else
            {
                indexToGoTo = playerVoidChestData.getFriendsIndex();
            }
            // Compute the player's X position based on the index
            int playerXBase = indexToGoTo * AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();
            // Set the player's position and rotation for some reason we have to use the connection object to send a packet instead of just using entityplayer#setPosition
            entityPlayer.connection.setPlayerLocation(playerXBase + 24.5, 104, 3, 0, 0);
        }

        // If the player left the void chest reset their position
        if (dimensionFrom == ModDimensions.VOID_CHEST.getId())
        {
            // Grab the player's pre-teleport position
            BlockPos preTeleportPosition = entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null).getPreTeleportPosition();
            // Reset the player's position
            entityPlayer.connection.setPlayerLocation(preTeleportPosition.getX() + 0.5, preTeleportPosition.getY() + 1.5, preTeleportPosition.getZ() + 0.5, 0, 0);
        }
    }
}
