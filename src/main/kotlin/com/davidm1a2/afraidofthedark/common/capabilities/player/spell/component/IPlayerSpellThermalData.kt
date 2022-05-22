package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

interface IPlayerSpellThermalData {
    var vitae: Double
    var heat: Double

    /**
     * Gets the current max thermal vitae the player may have
     *
     * @param world The world to check
     * @return A positive vitae amount
     */
    fun getMaxVitae(world: World): Double

    /**
     * Synchronizes thermal data between server and client. Only needs to sync vitae not heat
     *
     * @param entityPlayer The player to sync thermal data to
     */
    fun sync(entityPlayer: PlayerEntity)
}