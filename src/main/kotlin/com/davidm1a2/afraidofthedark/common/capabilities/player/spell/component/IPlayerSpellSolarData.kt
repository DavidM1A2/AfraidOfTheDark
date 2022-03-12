package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.entity.player.PlayerEntity

interface IPlayerSpellSolarData {
    var vitae: Double

    /**
     * Synchronizes solar data between server and client
     *
     * @param entityPlayer The player to sync solar data to
     */
    fun sync(entityPlayer: PlayerEntity)
}