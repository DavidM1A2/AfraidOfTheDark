package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.DavidM1A2.afraidofthedark.common.dimension.voidChest.VoidChestUtility;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Class used to detect player change dimension events and implement dimension logic
 */
public class TeleportHandler
{
	private static final int VALID_POS_SEARCH_DISTANCE = 6;

	/**
	 * Called when any entity wants to change dimensions, we only care about players in this case
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
			if (event.getEntity() instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();
				// Process the pre-teleport server side, if it returns true then we cancel the TP
				if (this.processPreTeleport(entityPlayer, fromDimension, toDimension))
					event.setCanceled(true);
			}
		}
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
			EntityPlayer entityPlayer = event.player;
			// Process the post-teleport server side
			this.processPostTeleport(entityPlayer, fromDimension, toDimension);
		}
	}

	/**
	 * Called right before the player is about to teleport
	 *
	 * @param entityPlayer The teleporting player
	 * @param dimensionFrom The dimension the player is currently in
	 * @param dimensionTo The dimension the player is going to
	 * @return True to cancel the teleport, false otherwise
	 */
	private boolean processPreTeleport(EntityPlayer entityPlayer, int dimensionFrom, int dimensionTo)
	{
		// If we're going to dimension VOID_CHEST then we need to do some preprocesing and tests to ensure the player can continue
		if (dimensionTo == ModDimensions.VOID_CHEST.getId())
		{
			// We can't go from void chest to void chest
			if (dimensionFrom == ModDimensions.VOID_CHEST.getId())
				return true;

			// Any other dimension is valid. We can go from any dimension other than the void_chest to the void_chest
			// We need to store off player position data pre-teleport
			IAOTDPlayerVoidChestData playerVoidChestData = entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null);
			// Compute a pre-teleport position
			int xCenter = entityPlayer.getPosition().getX();
			int yCenter = entityPlayer.getPosition().getY();
			int zCenter = entityPlayer.getPosition().getZ();
			// Test for a valid spot within ~6 blocks of the player's position. This is used to ensure players do not come back to the overworld and straight into a
			// new portal block. This ensure you don't get stuck in a teleport loop
			// First just test the player's current position, if it's invalid search in a +/- 6 block radius in all directions for a valid position
			if (this.isValidSpawnLocation(entityPlayer.world, xCenter, yCenter, zCenter))
				playerVoidChestData.setPreTeleportPosition(new BlockPos(xCenter, yCenter, zCenter));
			else
			{
				boolean foundSpot = false;
				for (int x = xCenter - VALID_POS_SEARCH_DISTANCE / 2; x < xCenter + VALID_POS_SEARCH_DISTANCE / 2 && !foundSpot; x++)
					for (int y = yCenter - VALID_POS_SEARCH_DISTANCE / 2; y < yCenter + VALID_POS_SEARCH_DISTANCE / 2 && !foundSpot; y++)
						for (int z = zCenter - VALID_POS_SEARCH_DISTANCE / 2; z < zCenter + VALID_POS_SEARCH_DISTANCE / 2 && !foundSpot; z++)
							if (this.isValidSpawnLocation(entityPlayer.world, x, y, z))
							{
								playerVoidChestData.setPreTeleportPosition(new BlockPos(x, y, z));
								foundSpot = true;
							}
				// If we didn't find a valid spot around the player's position then throw an error and set the player's spawn spot to x, 255, z as default
				if (!foundSpot)
				{
					entityPlayer.sendMessage(new TextComponentString("An error occoured when computing a re-spawn position upon returning to your dimension later so the teleport can't happen. Try finding a more open place to enter the portal/chest at!"));
					return true;
				}
			}
			// Set our pre-teleport dimension ID
			playerVoidChestData.setPreTeleportDimensionID(dimensionFrom);
		}
		return false;
	}

	/**
	 * Tests if a given x,y,z spot in the world is a valid place to spawn at
	 *
	 * @param world The world to test
	 * @param x The x coordinate of the spot
	 * @param y The y coordinate of the spot
	 * @param z The z coordinate of the spot
	 * @return True if the spot has a solid floor and air above, false otherwise
	 */
	private boolean isValidSpawnLocation(World world, int x, int y, int z)
	{
		BlockPos bottomPos = new BlockPos(x, y, z);
		IBlockState bottomBlock = world.getBlockState(bottomPos);
		Material bottomBlockMaterial = bottomBlock.getMaterial();
		// If the material is solid and blocks movement it's valid
		if (bottomBlockMaterial.isSolid() && bottomBlockMaterial.blocksMovement())
			// Ensure the two blocks above are air
			return world.getBlockState(bottomPos.up()).getBlock() instanceof BlockAir && world.getBlockState(bottomPos.up(2)).getBlock() instanceof BlockAir;
		return false;
	}

	/**
	 * Called right after the player teleported
	 *
	 * @param entityPlayer The teleporting player
	 * @param dimensionFrom The dimension the player was in
	 * @param dimensionTo The dimension the player is now in
	 */
	private void processPostTeleport(EntityPlayer entityPlayer, int dimensionFrom, int dimensionTo)
	{
		// If the player entered the void chest dimension then set their position
		if (dimensionTo == ModDimensions.VOID_CHEST.getId())
		{
			// Grab the player's void chest data
			IAOTDPlayerVoidChestData playerVoidChestData = entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null);
			// If the player was traveling to a friend's void chest grab that index, otherwise grab our own index
			int indexToGoTo;
			// If the friend's index is -1 then we go to our position, otherwise go to the friends position. Also wipe out the friends index variable
			if (playerVoidChestData.getFriendsIndex() == -1)
				// Get or compute the player's index to go to based on who the furthest out player is
				indexToGoTo = VoidChestUtility.getOrAssignPlayerPositionalIndex(entityPlayer.getServer(), entityPlayer);
			else
			{
				indexToGoTo = playerVoidChestData.getFriendsIndex();
				// Reset the friends index to -1
				playerVoidChestData.setFriendsIndex(-1);
			}
			// Compute the player's X position based on the index
			int playerXBase = indexToGoTo * AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();
			// Set the player's position and rotation for some reason we have to use the connection object to send a packet instead of just using entityplayer#setPosition
			((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(playerXBase + 24.5, 104, 3, 0, 0);
		}

		// If the player left the void chest reset their position
		if (dimensionFrom == ModDimensions.VOID_CHEST.getId())
		{
			// Grab the player's pre-teleport position
			BlockPos preTeleportPosition = entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null).getPreTeleportPosition();
			// Reset the player's position
			((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(preTeleportPosition.getX() + 0.5, preTeleportPosition.getY() + 1.5, preTeleportPosition.getZ() + 0.5, 0, 0);
		}
	}
}
