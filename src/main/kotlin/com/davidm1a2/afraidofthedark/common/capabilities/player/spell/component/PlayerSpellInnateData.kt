package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellInnateDataPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

class PlayerSpellInnateData : IPlayerSpellInnateData {
    override var vitae: Double = 0.0

    private fun isServerSide(entityPlayer: Player): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun getMaxVitae(entityPlayer: Player): Double {
        return 20.0
    }

    override fun sync(entityPlayer: Player) {
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                SpellInnateDataPacket(vitae),
                entityPlayer as ServerPlayer
            )
        }
    }
}