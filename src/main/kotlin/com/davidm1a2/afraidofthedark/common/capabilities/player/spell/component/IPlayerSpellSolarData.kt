package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

interface IPlayerSpellSolarData {
    var vitae: Double

    /**
     * Gets the current max solar vitae the player may have
     *
     * @param world The world to check
     * @return A positive vitae amount
     */
    fun getMaxVitae(world: Level): Double

    /**
     * Synchronizes solar data between server and client
     *
     * @param entityPlayer The player to sync solar data to
     */
    fun sync(entityPlayer: Player)
}