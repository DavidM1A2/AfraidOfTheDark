package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.*
import com.davidm1a2.afraidofthedark.common.network.packets.other.*
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
            packetHandler.registerPacket(UpdateSelectedPowerSourcePacket::class.java, UpdateSelectedPowerSourcePacketProcessor())
            packetHandler.registerPacket(SpellPacket::class.java, SpellPacketProcessor())
            packetHandler.registerPacket(ClearSpellsPacket::class.java, ClearSpellsPacketProcessor())
            packetHandler.registerPacket(AnimationPacket::class.java, AnimationPacketProcessor())
            packetHandler.registerPacket(CooldownSyncPacket::class.java, CooldownSyncPacketProcessor())
            packetHandler.registerPacket(VoidChestPacket::class.java, VoidChestPacketProcessor())
            packetHandler.registerPacket(ParticlePacket::class.java, ParticlePacketProcessor())
            packetHandler.registerPacket(SpellFreezeDataPacket::class.java, SpellFreezeDataPacketProcessor())
            packetHandler.registerPacket(PlayEnariasFightMusicPacket::class.java, PlayEnariasFightMusicPacketProcessor())
            packetHandler.registerPacket(FireWristCrossbowPacket::class.java, FireWristCrossbowPacketProcessor())
            packetHandler.registerPacket(ProcessSextantInputPacket::class.java, ProcessSextantInputPacketProcessor())
            packetHandler.registerPacket(SpellKeyPressPacket::class.java, SpellKeyPressPacketProcessor())
            packetHandler.registerPacket(CheatSheetUnlockPacket::class.java, CheatSheetUnlockPacketProcessor())
            packetHandler.registerPacket(WardBlocksPacket::class.java, WardBlocksPacketProcessor())
            packetHandler.registerPacket(SpellLunarDataPacket::class.java, SpellLunarDataPacketProcessor())
            packetHandler.registerPacket(SpellSolarDataPacket::class.java, SpellSolarDataPacketProcessor())
            packetHandler.registerPacket(SpellThermalDataPacket::class.java, SpellThermalDataPacketProcessor())
            packetHandler.registerPacket(SpellInnateDataPacket::class.java, SpellInnateDataPacketProcessor())
        }
    }
}