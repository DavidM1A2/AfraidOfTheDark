package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacket
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.AOTDPlayerBasicsPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.AOTDPlayerBasicsPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.ClearSpellsPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.ClearSpellsPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.FreezeDataPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.FreezeDataPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.ResearchPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.ResearchPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.SelectedWristCrossbowBoltPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.SelectedWristCrossbowBoltPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.SpellPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.SpellPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.StartAOTDPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets.StartAOTDPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.CooldownSyncPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.CooldownSyncPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.FireWristCrossbowPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.FireWristCrossbowPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ParticlePacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ParticlePacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.PlayEnariasFightMusicPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.PlayEnariasFightMusicPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ProcessSextantInputPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ProcessSextantInputPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.SpellKeyPressPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.SpellKeyPressPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.UpdateWatchedMeteorPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.UpdateWatchedMeteorPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.VoidChestPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.VoidChestPacketProcessor
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

/**
 * Class that registers all of our mod's packets
 */
class PacketRegister {
    /**
     * Registers all the mod's packets
     */
    @SubscribeEvent
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            val packetHandler = AfraidOfTheDark.packetHandler
            packetHandler.registerPacket(StartAOTDPacket::class.java, StartAOTDPacketProcessor())
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
}