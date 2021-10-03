package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.entity.player.PlayerEntity
import java.time.ZonedDateTime

/**
 * An interface that stores all known research for a given player
 */
interface IPlayerResearch {
    /**
     * Returns true if a research is researched by a player or false otherwise
     *
     * @param research The research to test
     * @return True if the research is researched, or false otherwise
     */
    fun isResearched(research: Research): Boolean

    /**
     * Returns the time the research was researched, or null if it is not researched
     *
     * @param research The research to test
     * @return The time the research was researched, or null if it is not researched
     */
    fun getResearchTime(research: Research): ZonedDateTime?

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
     * @param research The research to unlock
     * @param researchTime The time the research was researched, or null if it is not researched
     */
    fun setResearch(research: Research, researchTime: ZonedDateTime?)

    /**
     * Synchronizes research between server and client
     *
     * @param entityPlayer The player to sync research to
     * @param notify True if the player should be notified of any new researches, false otherwise
     */
    fun sync(entityPlayer: PlayerEntity, notify: Boolean)
}