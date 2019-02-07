package com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import net.minecraft.util.math.BlockPos;

/**
 * Default implementation of the AOTD player void chest capability
 */
public class AOTDPlayerVoidChestDataImpl implements IAOTDPlayerVoidChestData
{
	// Int telling us what index in the void chest dimension this player owns. Initialize to -1 so that
	// We can test if the player has a position so far or not
	private int positionIndex = -1;
	// The position of the player before teleporting to the void chest
	private BlockPos preTeleportPosition;
	// The dimension id of the player before teleporting to the void chest
	private int preTeleportDimensionID;

	/**
	 * @return the index corresponding to the player's void chest position in the void chest dimension
	 */
	@Override
	public int getPositionalIndex()
	{
		return this.positionIndex;
	}

	/**
	 * Sets the index corresponding to the player's void chest position in the void chest dimension
	 *
	 * @param locationIndex The index to set
	 */
	@Override
	public void setPositionalIndex(int locationIndex)
	{
		this.positionIndex = locationIndex;
	}

	/**
	 * @return the position in world that the player was at before teleporting into the void chest
	 */
	@Override
	public BlockPos getPreTeleportPosition()
	{
		return this.preTeleportPosition;
	}

	/**
	 * Sets the position in the world that the player was at before teleporting into the void chest
	 *
	 * @param blockPos The new position
	 */
	@Override
	public void setPreTeleportPosition(BlockPos blockPos)
	{
		this.preTeleportPosition = blockPos;
	}

	/**
	 * @return The ID of the dimension the player was in before teleporting to the void chest
	 */
	@Override
	public int getPreTeleportDimensionID()
	{
		return this.preTeleportDimensionID;
	}

	/**
	 * Sets the ID of the dimension the player was in before teleporting to the void chest
	 *
	 * @param dimensionID The dimension ID
	 */
	@Override
	public void setPreTeleportDimensionID(int dimensionID)
	{
		this.preTeleportDimensionID = dimensionID;
	}
}
