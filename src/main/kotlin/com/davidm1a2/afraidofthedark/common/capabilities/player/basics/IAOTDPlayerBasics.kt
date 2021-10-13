package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation

/**
 * An interface that is a base for AOTD player basic capabilities
 *
 * @property selectedWristCrossbowBoltIndex the selected wrist crossbow bolt index
 */
interface IAOTDPlayerBasics {
    var selectedWristCrossbowBoltIndex: Int
    var watchedMeteor: WatchedMeteor?

    /**
     * Called by a client to start the mod
     *
     * @param entityPlayer The player that started the mod
     */
    fun startAOTD(entityPlayer: PlayerEntity)

    /**
     * Sets the multiplicity of some key to some value. Used by the MultiplicityResearchTrigger
     *
     * @param key The key to set the multiplicity of
     * @param value The value to set the multiplicity to
     */
    fun setMultiplicity(key: ResourceLocation, value: Int)

    /**
     * Gets the multiplicity of some key. Used by the MultiplicityResearchTrigger
     *
     * @param key The key to get the multiplicity of
     * @return The multiplicity value, or 0 if it does not exist
     */
    fun getMultiplicity(key: ResourceLocation): Int

    /**
     * Lists all multiplicity keys. Used in serialization
     *
     * @return A list of all multiplicity keys
     */
    fun listMultiplicities(): List<ResourceLocation>

    /**
     * Syncs the newly selected crossbow bolt to the server
     *
     * @param entityPlayer The player to sync for
     */
    fun syncSelectedWristCrossbowBoltIndex(entityPlayer: PlayerEntity)

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