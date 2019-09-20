package com.davidm1a2.afraidofthedark.common.capabilities.player.basics;

import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry;
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
     * Gets the selected wrist crossbow bolt index
     *
     * @return The selected wrist crossbow index
     */
    int getSelectedWristCrossbowBoltIndex();

    /**
     * Sets the selected wrist crossbow bolt index, should be between 0 and AOTDBoltType.ordinal().length - 1
     *
     * @param index The index to select
     */
    void setSelectedWristCrossbowBoltIndex(int index);

    /**
     * Syncs the newly selected crossbow bolt to the server
     *
     * @param entityPlayer The player to sync for
     */
    void syncSelectedWristCrossbowBoltIndex(EntityPlayer entityPlayer);

    /**
     * Sets the meteor that the player is currently watching. All 3 int values
     * are simply fabricated but used later in the sextant to compute
     * actual minecraft coordinates.
     *
     * @param meteorEntry The meteor that the player is watching
     * @param dropAngle   The angle the meteor dropped in at
     * @param latitude    The latitude the meteor dropped in at
     * @param longitude   The longitude the meteor dropped in at
     */
    void setWatchedMeteor(MeteorEntry meteorEntry, int dropAngle, int latitude, int longitude);

    /**
     * @return The meteor that the player is watching or null if not present
     */
    MeteorEntry getWatchedMeteor();

    /**
     * @return The angle the meteor dropped in at or -1 if not present
     */
    int getWatchedMeteorDropAngle();

    /**
     * @return The latitude the meteor dropped at or -1 if not present
     */
    int getWatchedMeteorLatitude();

    /**
     * @return The longitude the meteor dropped at or -1 if not present
     */
    int getWatchedMeteorLongitude();

    /**
     * Syncs all the watched meteor data
     *
     * @param entityPlayer The player that the data is being synced for
     */
    void syncWatchedMeteor(EntityPlayer entityPlayer);

    /**
     * Syncs all player basic data from server -> client
     *
     * @param entityPlayer The player to sync to
     */
    void syncAll(EntityPlayer entityPlayer);
}
