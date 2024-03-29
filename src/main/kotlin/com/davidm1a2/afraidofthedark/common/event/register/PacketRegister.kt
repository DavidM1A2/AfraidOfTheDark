package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.AOTDPlayerBasicsPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.AOTDPlayerBasicsPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.ClearSpellsPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.ClearSpellsPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.ResearchPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.ResearchPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellFreezeDataPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellFreezeDataPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellInnateDataPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellInnateDataPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellLunarDataPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellLunarDataPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellSolarDataPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellSolarDataPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellThermalDataPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellThermalDataPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.StartAOTDPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.StartAOTDPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.capability.WardBlocksPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.WardBlocksPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.CheatSheetUnlockPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.CheatSheetUnlockPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.CooldownSyncPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.CooldownSyncPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.FireWristCrossbowPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.FireWristCrossbowPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.PlayEnariasFightMusicPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.PlayEnariasFightMusicPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.ProcessSextantInputPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.ProcessSextantInputPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.SpellKeyPressPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.SpellKeyPressPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.UpdateSelectedPowerSourcePacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.UpdateSelectedPowerSourcePacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.UpdateWatchedMeteorPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.UpdateWatchedMeteorPacketProcessor
import com.davidm1a2.afraidofthedark.common.network.packets.other.VoidChestPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.VoidChestPacketProcessor
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

/**
 * Class that registers all of our mod's packets
 */
class PacketRegister(private val researchOverlayHandler: ResearchOverlayHandler) {
    /**
     * Registers all the mod's packets
     */
    @SubscribeEvent
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            val packetHandler = AfraidOfTheDark.packetHandler
            packetHandler.registerPacket(StartAOTDPacket::class.java, StartAOTDPacketProcessor())
            packetHandler.registerPacket(AOTDPlayerBasicsPacket::class.java, AOTDPlayerBasicsPacketProcessor())
            packetHandler.registerPacket(ResearchPacket::class.java, ResearchPacketProcessor(researchOverlayHandler))
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