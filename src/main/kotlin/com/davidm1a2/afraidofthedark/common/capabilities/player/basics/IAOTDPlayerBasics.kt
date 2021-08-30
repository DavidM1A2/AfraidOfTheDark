package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.common.registry.MeteorEntry
import net.minecraft.entity.player.PlayerEntity

/**
 * An interface that is a base for AOTD player basic capabilities
 *
 * @property selectedWristCrossbowBoltIndex the selected wrist crossbow bolt index
 */
interface IAOTDPlayerBasics {
    var selectedWristCrossbowBoltIndex: Int

    /**
     * Called by a client to start the mod
     *
     * @param entityPlayer The player that started the mod
     */
    fun startAOTD(entityPlayer: PlayerEntity)

    /**
     * Syncs the newly selected crossbow bolt to the server
     *
     * @param entityPlayer The player to sync for
     */
    fun syncSelectedWristCrossbowBoltIndex(entityPlayer: PlayerEntity)

    /**
     * Sets the meteor that the player is currently watching. All 3 int values
     * are simply fabricated but used later in the sextant to compute
     * actual minecraft coordinates.
     *
     * @param meteorEntry The meteor that the player is watching
     * @param accuracy The accuracy of the telescope used to track the meteor
     * @param dropAngle   The angle the meteor dropped in at
     * @param latitude    The latitude the meteor dropped in at
     * @param longitude   The longitude the meteor dropped in at
     */
    fun setWatchedMeteor(meteorEntry: MeteorEntry?, accuracy: Int, dropAngle: Int, latitude: Int, longitude: Int)

    /**
     * @return The meteor that the player is watching or null if not present
     */
    fun getWatchedMeteor(): MeteorEntry?

    /**
     * @return The accuracy of the telescope used to track the meteor
     */
    fun getWatchedMeteorAccuracy(): Int

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
    fun syncWatchedMeteor(entityPlayer: PlayerEntity)

    /**
     * Syncs all player basic data from server -> client
     *
     * @param entityPlayer The player to sync to
     */
    fun syncAll(entityPlayer: PlayerEntity)
}