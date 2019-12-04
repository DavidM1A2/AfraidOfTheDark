package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry
import net.minecraft.entity.player.EntityPlayer

/**
 * An interface that is a base for AOTD player basic capabilities
 *
 * @property startedAOTD True if the player has started the afraid of the dark mod, false otherwise
 * @property selectedWristCrossbowBoltIndex the selected wrist crossbow bolt index
 */
interface IAOTDPlayerBasics
{
    var startedAOTD: Boolean
    var selectedWristCrossbowBoltIndex: Int

    /**
     * Called to either tell client -> server the current client AOTD status or server -> client based on if it's client or server side
     *
     * @param entityPlayer The player to sync
     */
    fun syncStartedAOTD(entityPlayer: EntityPlayer)

    /**
     * Syncs the newly selected crossbow bolt to the server
     *
     * @param entityPlayer The player to sync for
     */
    fun syncSelectedWristCrossbowBoltIndex(entityPlayer: EntityPlayer)

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
    fun setWatchedMeteor(meteorEntry: MeteorEntry?, dropAngle: Int, latitude: Int, longitude: Int)

    /**
     * @return The meteor that the player is watching or null if not present
     */
    fun getWatchedMeteor(): MeteorEntry?

    /**
     * @return The angle the meteor dropped in at or -1 if not present
     */
    fun getWatchedMeteorDropAngle(): Int

    /**
     * @return The latitude the meteor dropped at or -1 if not present
     */
    fun getWatchedMeteorLatitude(): Int

    /**
     * @return The longitude the meteor dropped at or -1 if not present
     */
    fun getWatchedMeteorLongitude(): Int

    /**
     * Syncs all the watched meteor data
     *
     * @param entityPlayer The player that the data is being synced for
     */
    fun syncWatchedMeteor(entityPlayer: EntityPlayer)

    /**
     * Syncs all player basic data from server -> client
     *
     * @param entityPlayer The player to sync to
     */
    fun syncAll(entityPlayer: EntityPlayer)
}