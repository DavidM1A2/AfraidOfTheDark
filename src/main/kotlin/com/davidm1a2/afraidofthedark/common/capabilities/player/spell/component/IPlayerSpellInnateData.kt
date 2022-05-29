package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.entity.player.PlayerEntity

interface IPlayerSpellInnateData {
    var vitae: Double

    /**
     * Gets the current max innate vitae the player may have
     *
     * @param entityPlayer The player to check
     * @return A positive vitae amount
     */
    fun getMaxVitae(entityPlayer: PlayerEntity): Double

    /**
     * Synchronizes innate data between server and client
     *
     * @param entityPlayer The player to sync innate data to
     */
    fun sync(entityPlayer: PlayerEntity)
}