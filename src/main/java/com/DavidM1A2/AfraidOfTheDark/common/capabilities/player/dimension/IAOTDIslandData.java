package com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension;

/**
 * Base interface for AOTD island data use for nightmare and void chest data
 */
public interface IAOTDIslandData
{
    /**
     * @return the index corresponding to the player's island position
     */
    int getPositionalIndex();

    /**
     * Sets the index corresponding to the player's island position
     *
     * @param locationIndex The index to set
     */
    void setPositionalIndex(int locationIndex);
}
