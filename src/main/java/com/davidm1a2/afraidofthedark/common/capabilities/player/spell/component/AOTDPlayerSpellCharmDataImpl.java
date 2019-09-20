package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component;

import java.util.UUID;

/**
 * Default implementation of the AOTD spell charm data class
 */
public class AOTDPlayerSpellCharmDataImpl implements IAOTDPlayerSpellCharmData
{
    // The number of charm ticks remaining
    private int charmTicksRemaining = 0;
    // The id of the entity that is doing the charming
    private UUID charmingEntityId;

    /**
     * Sets the number of ticks the player should be charmed
     *
     * @param numberOfTicks The number of ticks
     */
    @Override
    public void setCharmTicks(int numberOfTicks)
    {
        this.charmTicksRemaining = numberOfTicks;
    }

    /**
     * Gets the number of ticks the player should be charmed
     *
     * @return The number of ticks
     */
    @Override
    public int getCharmTicks()
    {
        return this.charmTicksRemaining;
    }

    /**
     * Sets the charmingEntityId that is doing the charming
     *
     * @param charmingEntityId The charmingEntityId that is doing the charming
     */
    @Override
    public void setCharmingEntityId(UUID charmingEntityId)
    {
        this.charmingEntityId = charmingEntityId;
    }

    /**
     * Gets the entity that is doing the charming
     *
     * @return The entity that is doing the charming
     */
    @Override
    public UUID getCharmingEntityId()
    {
        return this.charmingEntityId;
    }
}
