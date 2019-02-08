package com.DavidM1A2.afraidofthedark.common.dimension.voidChest;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.VoidChestData;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * Utility methods that deal with void chest related things
 */
public class VoidChestUtility
{
	/**
	 * Called to get a player's void chest positional index. If none is present one will be computed first.
	 *
	 * @param minecraftServer The minecraft server used in computing the player's positional index
	 * @param entityPlayer The player to get the index for
	 * @return The position in the void chest that this player owns
	 */
	public static int getOrAssignPlayerPositionalIndex(MinecraftServer minecraftServer, EntityPlayer entityPlayer)
	{
		IAOTDPlayerVoidChestData playerVoidChestData = entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null);
		// -1 means unassigned, we need to compute the index first
		if (playerVoidChestData.getPositionalIndex() == -1)
		{
			// Compute this new player's index
			int playersNewPositionalIndex = VoidChestData.get(minecraftServer.getWorld(ModDimensions.VOID_CHEST.getId())).addAndReturnNewVisitor();
			// Set that new index
			playerVoidChestData.setPositionalIndex(playersNewPositionalIndex);
		}
		return playerVoidChestData.getPositionalIndex();
	}
}
