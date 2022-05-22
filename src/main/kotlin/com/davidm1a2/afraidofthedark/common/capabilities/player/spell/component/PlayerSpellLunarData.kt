package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capability.LunarDataPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.World

class PlayerSpellLunarData : IPlayerSpellLunarData {
    override var vitae: Double = 0.0

    private fun isServerSide(entityPlayer: PlayerEntity): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun getMaxVitae(world: World): Double {
        val moonPhase = world.dimensionType().moonPhase(world.dayTime())
        return VITAE_CAP_BY_MOON_PHASE[moonPhase] ?: 0.0
    }

    override fun sync(entityPlayer: PlayerEntity) {
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                LunarDataPacket(vitae),
                entityPlayer as ServerPlayerEntity
            )
        }
    }

    companion object {
        // The maximum amount of vitae we can store in each moon phase
        private val VITAE_CAP_BY_MOON_PHASE = mapOf(
            0 to 2000.0, // Full (100%)
            1 to 300.0, // Waning Gibbous (75%)
            2 to 100.0, // Third Quarter (50%)
            3 to 30.0, // Waning Crescent (25%)
            4 to 10.0, // New Moon (0%)
            5 to 30.0, // Waxing Crescent (25%)
            6 to 100.0, // First Quarter (50%)
            7 to 300.0 // Waxing Gibbous (75%)
        )
    }
}