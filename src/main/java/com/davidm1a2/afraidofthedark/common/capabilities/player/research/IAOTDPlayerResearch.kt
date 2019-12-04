package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.player.EntityPlayer

/**
 * An interface that stores all known research for a given player
 */
interface IAOTDPlayerResearch
{
    /**
     * Returns true if a research is researched by a player or false otherwise
     *
     * @param research The research to test
     * @return True if the research is researched, or false otherwise
     */
    fun isResearched(research: Research): Boolean

    /**
     * Returns true if the user can research a research or false if not
     *
     * @param research The research to test
     * @return True if the player can research a given research or false otherwise
     */
    fun canResearch(research: Research): Boolean

    /**
     * Sets a given research to be unlocked or not
     *
     * @param research   The research to unlock
     * @param researched If the research is researched or not
     */
    fun setResearch(research: Research, researched: Boolean)

    /**
     * Sets a given research to be unlocked or not and shows the player a popup that notifies them of the unlock
     *
     * @param research     The research to unlock
     * @param researched   If the research is researched or not
     * @param entityPlayer The player to alert of the research
     */
    fun setResearchAndAlert(research: Research, researched: Boolean, entityPlayer: EntityPlayer)

    /**
     * Synchronizes research between server and client
     *
     * @param entityPlayer The player to sync research to
     * @param notify       True if the player should be notified of any new researches, false otherwise
     */
    fun sync(entityPlayer: EntityPlayer, notify: Boolean)
}