package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerStartedAfraidOfTheDarkEvent
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.AOTDPlayerBasicsPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.SelectedWristCrossbowBoltPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.StartAOTDPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.UpdateWatchedMeteorPacket
import com.davidm1a2.afraidofthedark.common.registry.MeteorEntry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.common.MinecraftForge

/**
 * Default implementation of the AOTD player basics capability
 *
 * @property selectedWristCrossbowBoltIndex Integer telling us what bolt the wrist crossbow is currently set to fire
 * @property watchedMeteor The meteor the player is currently observing (was last clicked in telescope)
 * @property watchedMeteorAccuracy The accuracy of the telescope used to watch the meteor
 * @property watchedMeteorDropAngle <\
 * @property watchedMeteorLatitude    - The  3 properties of the watched meteor
 * @property watchedMeteorLongitude </
 */
class AOTDPlayerBasics : IAOTDPlayerBasics {
    override var selectedWristCrossbowBoltIndex = 0
    private var watchedMeteor: MeteorEntry? = null
    private var watchedMeteorAccuracy: Int = 0
    private var watchedMeteorDropAngle: Int = -1
    private var watchedMeteorLatitude: Int = -1
    private var watchedMeteorLongitude: Int = -1

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private fun isServerSide(entityPlayer: PlayerEntity): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun startAOTD(entityPlayer: PlayerEntity) {
        if (isServerSide(entityPlayer)) {
            throw IllegalStateException("Only a client can make a player start AOTD")
        }

        MinecraftForge.EVENT_BUS.post(PlayerStartedAfraidOfTheDarkEvent(entityPlayer))
        AfraidOfTheDark.packetHandler.sendToServer(StartAOTDPacket())
    }

    /**
     * Syncs the newly selected crossbow bolt to the server
     *
     * @param entityPlayer The player to sync for
     */
    override fun syncSelectedWristCrossbowBoltIndex(entityPlayer: PlayerEntity) {
        // Can only send this client -> server side
        if (!isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendToServer(
                SelectedWristCrossbowBoltPacket(
                    selectedWristCrossbowBoltIndex
                )
            )
        }
    }

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
    override fun setWatchedMeteor(meteorEntry: MeteorEntry?, accuracy: Int, dropAngle: Int, latitude: Int, longitude: Int) {
        watchedMeteor = meteorEntry
        watchedMeteorAccuracy = accuracy
        watchedMeteorDropAngle = dropAngle
        watchedMeteorLatitude = latitude
        watchedMeteorLongitude = longitude
    }

    /**
     * @return The meteor that the player is watching or null if not present
     */
    override fun getWatchedMeteor(): MeteorEntry? {
        return watchedMeteor
    }

    /**
     * @return The accuracy of the telescope used to track the meteor
     */
    override fun getWatchedMeteorAccuracy(): Int {
        return watchedMeteorAccuracy
    }

    /**
     * @return The angle the meteor dropped in at or -1 if not present
     */
    override fun getWatchedMeteorDropAngle(): Int {
        return watchedMeteorDropAngle
    }

    /**
     * @return The latitude the meteor dropped at or -1 if not present
     */
    override fun getWatchedMeteorLatitude(): Int {
        return watchedMeteorLatitude
    }

    /**
     * @return The longitude the meteor dropped at or -1 if not present
     */
    override fun getWatchedMeteorLongitude(): Int {
        return watchedMeteorLongitude
    }

    /**
     * Syncs all the watched meteor data
     *
     * @param entityPlayer The player that the data is being synced for
     */
    override fun syncWatchedMeteor(entityPlayer: PlayerEntity) {
        // If we're on server side send the client the meteor data
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                UpdateWatchedMeteorPacket(
                    watchedMeteor,
                    watchedMeteorAccuracy,
                    watchedMeteorDropAngle,
                    watchedMeteorLatitude,
                    watchedMeteorLongitude
                ), entityPlayer as ServerPlayerEntity
            )
        }
    }

    /**
     * Syncs all player basic data from server -> client
     *
     * @param entityPlayer The player to sync to
     */
    override fun syncAll(entityPlayer: PlayerEntity) {
        // If we're on server side send the player's data
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(AOTDPlayerBasicsPacket(this), entityPlayer as ServerPlayerEntity)
        } else {
            AfraidOfTheDark.packetHandler.sendToServer(AOTDPlayerBasicsPacket(this))
        }
    }
}