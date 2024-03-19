package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellLunarDataPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class PlayerSpellLunarData : IPlayerSpellLunarData {
    override var vitae: Double = 0.0

    private fun isServerSide(entityPlayer: Player): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun getMaxVitae(world: Level): Double {
        val moonPhase = world.dimensionType().moonPhase(world.dayTime())
        return VITAE_CAP_BY_MOON_PHASE[moonPhase] ?: 0.0
    }

    override fun sync(entityPlayer: Player) {
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                SpellLunarDataPacket(vitae),
                entityPlayer as ServerPlayer
            )
        }
    }

    companion object {
        // The maximum amount of vitae we can store in each moon phase
        private val VITAE_CAP_BY_MOON_PHASE = mapOf(
            0 to 20000.0, // Full (100%)
            1 to 500.0, // Waning Gibbous (75%)
            2 to 300.0, // Third Quarter (50%)
            3 to 200.0, // Waning Crescent (25%)
            4 to 100.0, // New Moon (0%)
            5 to 200.0, // Waxing Crescent (25%)
            6 to 300.0, // First Quarter (50%)
            7 to 500.0 // Waxing Gibbous (75%)
        )
    }
}