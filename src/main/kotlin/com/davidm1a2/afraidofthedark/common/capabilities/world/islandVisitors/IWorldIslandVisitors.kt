package com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors

/**
 * Capability interface to store island visitor information in a world
 */
interface IWorldIslandVisitors {
    /**
     * Adds a new unique visitor to the world and returns the current number of visitors
     *
     * @return The index that the latest visitor should go to
     */
    fun addAndGetNewVisitor(): Int

    /**
     * Sets the number of visitors a world has gotten. Used in deserialization
     *
     * @param visitorCount The number of visitors a world has gotten
     */
    fun setNumberOfVisitors(visitorCount: Int)

    /**
     * Gets the number of visitors a world has gotten. Used in serialization
     *
     * @return The number of unique players that have gone to the world
     */
    fun getNumberOfVisitors(): Int
}