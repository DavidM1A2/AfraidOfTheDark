package com.DavidM1A2.afraidofthedark.common.capabilities.player.research;

import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraft.entity.player.EntityPlayer;

/**
 * An interface that stores all known research for a given player
 */
public interface IAOTDPlayerResearch
{
	/**
	 * Returns true if a research is researched by a player or false otherwise
	 *
	 * @param research The research to test
	 * @return True if the research is researched, or false otherwise
	 */
	boolean isResearched(Research research);

	/**
	 * Returns true if the user can research a research or false if not
	 *
	 * @param research The research to test
	 * @return True if the player can research a given research or false otherwise
	 */
	boolean canResearch(Research research);

	/**
	 * Sets a given research to be unlocked or not
	 *
	 * @param research The research to unlock
	 * @param researched If the research is researched or not
	 */
	void setResearch(Research research, boolean researched);

	/**
	 * Sets a given research to be unlocked or not and shows the player a popup that notifies them of the unlock
	 *
	 * @param research The research to unlock
	 * @param researched If the research is researched or not
	 * @param entityPlayer The player to alert of the research
	 */
	void setResearchAndAlert(Research research, boolean researched, EntityPlayer entityPlayer);

	/**
	 * Syncronizes research between server and client
	 *
	 * @param entityPlayer The player to sync research to
	 * @param notify True if the player should be notified of any new researches, false otherwise
	 */
	void sync(EntityPlayer entityPlayer, boolean notify);
}
