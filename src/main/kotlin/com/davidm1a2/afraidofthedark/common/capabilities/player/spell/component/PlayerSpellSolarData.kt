package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellSolarDataPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.World

class PlayerSpellSolarData : IPlayerSpellSolarData {
    override var vitae: Double = 0.0

    private fun isServerSide(entityPlayer: PlayerEntity): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun getMaxVitae(world: World): Double {
        return 200.0
    }

    override fun sync(entityPlayer: PlayerEntity) {
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                SpellSolarDataPacket(vitae),
                entityPlayer as ServerPlayerEntity
            )
        }
    }
}