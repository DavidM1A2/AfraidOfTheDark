package com.davidm1a2.afraidofthedark.common.capabilities.player.basics;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.SyncAOTDPlayerBasics;
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.SyncSelectedWristCrossbowBolt;
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.SyncStartedAOTD;
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.UpdateWatchedMeteor;
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Default implementation of the AOTD player basics capability
 */
public class AOTDPlayerBasicsImpl implements IAOTDPlayerBasics
{
    // Flag telling us if the user has started the mod or not
    private boolean startedAOTD;
    // Integer telling us what bolt the wrist crossbow is currently set to fire
    private int selectedWristCrossbowBoltIndex;
    // The meteor the player is currently observing (was last clicked in telescope)
    private MeteorEntry watchedMeteor;
    // The 3 properties of the watched meteor
    private int watchedMeteorDropAngle;
    private int watchedMeteorLatitude;
    private int watchedMeteorLongitude;

    /**
     * Constructor initializes default values
     */
    public AOTDPlayerBasicsImpl()
    {
        this.startedAOTD = false;
        this.selectedWristCrossbowBoltIndex = 0;
        this.watchedMeteor = null;
        this.watchedMeteorDropAngle = -1;
        this.watchedMeteorLatitude = -1;
        this.watchedMeteorLongitude = -1;
    }

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private boolean isServerSide(EntityPlayer entityPlayer)
    {
        return !entityPlayer.world.isRemote;
    }

    /**
     * @return True if the player has started the afraid of the dark mod, false otherwise
     */
    @Override
    public boolean getStartedAOTD()
    {
        return this.startedAOTD;
    }

    /**
     * Called to set true if the player has started the mod, false otherwise
     *
     * @param startedAOTD True if the player started AOTD, false otherwise
     */
    @Override
    public void setStartedAOTD(boolean startedAOTD)
    {
        this.startedAOTD = startedAOTD;
    }

    /**
     * Called to either tell client -> server the current client AOTD status or server -> client based on if it's client or server side
     *
     * @param entityPlayer The player to sync
     */
    @Override
    public void syncStartedAOTD(EntityPlayer entityPlayer)
    {
        if (this.isServerSide(entityPlayer))
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new SyncStartedAOTD(this.startedAOTD), (EntityPlayerMP) entityPlayer);
        }
        else
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new SyncStartedAOTD(this.startedAOTD));
        }
    }

    /**
     * Gets the selected wrist crossbow bolt index
     *
     * @return The selected wrist crossbow index
     */
    public int getSelectedWristCrossbowBoltIndex()
    {
        return this.selectedWristCrossbowBoltIndex;
    }

    /**
     * Sets the selected wrist crossbow bolt index, should be between 0 and AOTDBoltType.ordinal().length - 1
     *
     * @param index The index to select
     */
    public void setSelectedWristCrossbowBoltIndex(int index)
    {
        this.selectedWristCrossbowBoltIndex = index;
    }

    /**
     * Syncs the newly selected crossbow bolt to the server
     *
     * @param entityPlayer The player to sync for
     */
    public void syncSelectedWristCrossbowBoltIndex(EntityPlayer entityPlayer)
    {
        // Can only send this client -> server side
        if (!this.isServerSide(entityPlayer))
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new SyncSelectedWristCrossbowBolt(this.selectedWristCrossbowBoltIndex));
        }
    }

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
    @Override
    public void setWatchedMeteor(MeteorEntry meteorEntry, int dropAngle, int latitude, int longitude)
    {
        this.watchedMeteor = meteorEntry;
        this.watchedMeteorDropAngle = dropAngle;
        this.watchedMeteorLatitude = latitude;
        this.watchedMeteorLongitude = longitude;
    }

    /**
     * @return The meteor that the player is watching or null if not present
     */
    @Override
    public MeteorEntry getWatchedMeteor()
    {
        return this.watchedMeteor;
    }

    /**
     * @return The angle the meteor dropped in at or -1 if not present
     */
    @Override
    public int getWatchedMeteorDropAngle()
    {
        return this.watchedMeteorDropAngle;
    }

    /**
     * @return The latitude the meteor dropped at or -1 if not present
     */
    @Override
    public int getWatchedMeteorLatitude()
    {
        return this.watchedMeteorLatitude;
    }

    /**
     * @return The longitude the meteor dropped at or -1 if not present
     */
    @Override
    public int getWatchedMeteorLongitude()
    {
        return this.watchedMeteorLongitude;
    }

    /**
     * Syncs all the watched meteor data
     *
     * @param entityPlayer The player that the data is being synced for
     */
    @Override
    public void syncWatchedMeteor(EntityPlayer entityPlayer)
    {
        // If we're on server side send the client the meteor data
        if (this.isServerSide(entityPlayer))
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new UpdateWatchedMeteor(this.watchedMeteor, this.watchedMeteorDropAngle, this.watchedMeteorLatitude, this.watchedMeteorLongitude), (EntityPlayerMP) entityPlayer);
        }
    }

    /**
     * Syncs all player basic data from server -> client
     *
     * @param entityPlayer The player to sync to
     */
    @Override
    public void syncAll(EntityPlayer entityPlayer)
    {
        // If we're on server side send the player's data
        if (this.isServerSide(entityPlayer))
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new SyncAOTDPlayerBasics(this), (EntityPlayerMP) entityPlayer);
        }
        // If we're on client side request the server to send us player data
        else
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new SyncAOTDPlayerBasics(this));
        }
    }
}
