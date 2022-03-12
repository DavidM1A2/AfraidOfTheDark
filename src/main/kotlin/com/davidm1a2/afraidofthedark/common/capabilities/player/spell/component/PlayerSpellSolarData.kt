package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SolarDataPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity

class PlayerSpellSolarData : IPlayerSpellSolarData {
    override var vitae: Double = 0.0

    private fun isServerSide(entityPlayer: PlayerEntity): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun sync(entityPlayer: PlayerEntity) {
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                SolarDataPacket(vitae),
                entityPlayer as ServerPlayerEntity
            )
        }
    }
}