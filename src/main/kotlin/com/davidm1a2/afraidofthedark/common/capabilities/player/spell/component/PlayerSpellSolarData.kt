package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellSolarDataPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class PlayerSpellSolarData : IPlayerSpellSolarData {
    override var vitae: Double = 0.0

    private fun isServerSide(entityPlayer: Player): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun getMaxVitae(world: Level): Double {
        return 200.0
    }

    override fun sync(entityPlayer: Player) {
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                SpellSolarDataPacket(vitae),
                entityPlayer as ServerPlayer
            )
        }
    }
}