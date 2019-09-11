package com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component;

import java.util.UUID;

/**
 * An interface that defines any charm data stored on the player
 */
public interface IAOTDPlayerSpellCharmData
{
    /**
     * Sets the number of ticks the player should be charmed
     *
     * @param numberOfTicks The number of ticks
     */
    void setCharmTicks(int numberOfTicks);

    /**
     * Gets the number of ticks the player should be charmed
     *
     * @return The number of ticks
     */
    int getCharmTicks();

    /**
     * Sets the entity that is doing the charming
     *
     * @param charmingEntityId The id of the entity that is doing the charming
     */
    void setCharmingEntityId(UUID charmingEntityId);

    /**
     * Gets the entity that is doing the charming
     *
     * @return The id entity that is doing the charming
     */
    UUID getCharmingEntityId();
}
