package com.DavidM1A2.afraidofthedark.common.capabilities.player.basics;

import com.DavidM1A2.afraidofthedark.common.utility.AOTDBoltHelper;
import net.minecraft.entity.player.EntityPlayer;

/**
 * An interface that is a base for AOTD player basic capabilities
 */
public interface IAOTDPlayerBasics
{
	/**
	 * @return True if the player has started the afraid of the dark mod, false otherwise
	 */
	boolean getStartedAOTD();

	/**
	 * Called to set true if the player has started the mod, false otherwise
	 *
	 * @param startedAOTD True if the player started AOTD, false otherwise
	 */
	void setStartedAOTD(boolean startedAOTD);

	/**
	 * Called to either tell client -> server the current client AOTD status or server -> client based on if it's client or server side
	 *
	 * @param entityPlayer The player to sync
	 */
	void syncStartedAOTD(EntityPlayer entityPlayer);

	/**
	 * Sets the selected wrist crossbow bolt index, should be between 0 and AOTDBoltHelper.ordinal().length - 1
	 *
	 * @param index The index to select
	 */
	void setSelectedWristCrossbowBoltIndex(int index);

	/**
	 * Gets the selected wrist crossbow bolt index
	 *
	 * @return The selected wrist crossbow index
	 */
	int getSelectedWristCrossbowBoltIndex();

	/**
	 * Syncs the newly selected crossbow bolt to the server
	 *
	 * @param entityPlayer The player to sync for
	 */
	void syncSelectedWristCrossbowBoltIndex(EntityPlayer entityPlayer);

	/**
	 * Syncs all player basic data from server -> client
	 *
	 * @param entityPlayer The player to sync to
	 */
	void syncAll(EntityPlayer entityPlayer);
}
