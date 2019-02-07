package com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import scala.annotation.meta.param;

/**
 * An interface that is a base for AOTD player void chest data capabilities. This specific capability is only used server side!
 * This means no packets are needed to update the player about state changes and no sync method is present
 */
public interface IAOTDPlayerVoidChestData
{
	/**
	 * @return the index corresponding to the player's void chest position in the void chest dimension
	 */
	int getPositionalIndex();

	/**
	 * Sets the index corresponding to the player's void chest position in the void chest dimension
	 *
	 * @param locationIndex The index to set
	 */
	void setPositionalIndex(int locationIndex);

	/**
	 * @return the position in world that the player was at before teleporting into the void chest
	 */
	BlockPos getPreTeleportPosition();

	/**
	 * Sets the position in the world that the player was at before teleporting into the void chest
	 *
	 * @param blockPos The new position
	 */
	void setPreTeleportPosition(BlockPos blockPos);

	/**
	 * @return The ID of the dimension the player was in before teleporting to the void chest
	 */
	int getPreTeleportDimensionID();

	/**
	 * Sets the ID of the dimension the player was in before teleporting to the void chest
	 *
	 * @param dimensionID The dimension ID
	 */
	void setPreTeleportDimensionID(int dimensionID);
}
