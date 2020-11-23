package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacket
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.*
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.*

/**
 * Class that registers all of our mod's packets
 */
object PacketRegister {
    /**
     * Registers all the mod's packets
     */
    fun initialize() {
        val packetHandler = AfraidOfTheDark.packetHandler
        packetHandler.registerPacket(StartedAOTDPacket::class.java, StartedAOTDPacketProcessor())
        packetHandler.registerPacket(AOTDPlayerBasicsPacket::class.java, AOTDPlayerBasicsPacketProcessor())
        packetHandler.registerPacket(ResearchPacket::class.java, ResearchPacketProcessor())
        packetHandler.registerPacket(UpdateWatchedMeteorPacket::class.java, UpdateWatchedMeteorPacketProcessor())
        packetHandler.registerPacket(SpellPacket::class.java, SpellPacketProcessor())
        packetHandler.registerPacket(ClearSpellsPacket::class.java, ClearSpellsPacketProcessor())
        packetHandler.registerPacket(AnimationPacket::class.java, AnimationPacketProcessor())
        packetHandler.registerPacket(CooldownSyncPacket::class.java, CooldownSyncPacketProcessor())
        packetHandler.registerPacket(VoidChestPacket::class.java, VoidChestPacketProcessor())
        packetHandler.registerPacket(ParticlePacket::class.java, ParticlePacketProcessor())
        packetHandler.registerPacket(FreezeDataPacket::class.java, FreezeDataPacketProcessor())
        packetHandler.registerPacket(PlayEnariasFightMusicPacket::class.java, PlayEnariasFightMusicPacketProcessor())
        packetHandler.registerPacket(FireWristCrossbowPacket::class.java, FireWristCrossbowPacketProcessor())
        packetHandler.registerPacket(ProcessSextantInputPacket::class.java, ProcessSextantInputPacketProcessor())
        packetHandler.registerPacket(SelectedWristCrossbowBoltPacket::class.java, SelectedWristCrossbowBoltPacketProcessor())
        packetHandler.registerPacket(SpellKeyPressPacket::class.java, SpellKeyPressPacketProcessor())
    }
}