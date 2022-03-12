package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.entity.player.PlayerEntity

interface IPlayerSpellLunarData {
    var vitae: Double

    /**
     * Synchronizes lunar data between server and client
     *
     * @param entityPlayer The player to sync lunar data to
     */
    fun sync(entityPlayer: PlayerEntity)
}