package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

interface IPlayerSpellLunarData {
    var vitae: Double

    /**
     * Gets the current max lunar vitae the player may have
     *
     * @param world The world to check
     * @return A positive vitae amount
     */
    fun getMaxVitae(world: World): Double

    /**
     * Synchronizes lunar data between server and client
     *
     * @param entityPlayer The player to sync lunar data to
     */
    fun sync(entityPlayer: PlayerEntity)
}