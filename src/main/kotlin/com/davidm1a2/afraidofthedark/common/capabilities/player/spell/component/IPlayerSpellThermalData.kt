package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.entity.player.PlayerEntity

interface IPlayerSpellThermalData {
    var vitae: Double
    var heat: Double

    /**
     * Synchronizes thermal data between server and client. Only needs to sync vitae not heat
     *
     * @param entityPlayer The player to sync thermal data to
     */
    fun sync(entityPlayer: PlayerEntity)
}