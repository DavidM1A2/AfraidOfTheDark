package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.*
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.*
import net.minecraftforge.fml.relauncher.Side

/**
 * Class that registers all of our mod's packets
 */
object PacketRegister {
    /**
     * Registers all the mod's packets
     */
    fun initialize() {
        val packetHandler = AfraidOfTheDark.INSTANCE.packetHandler
        packetHandler.registerBidiPacket(SyncStartedAOTD::class.java, SyncStartedAOTD.Handler())
        packetHandler.registerBidiPacket(SyncAOTDPlayerBasics::class.java, SyncAOTDPlayerBasics.Handler())
        packetHandler.registerBidiPacket(SyncResearch::class.java, SyncResearch.Handler())
        packetHandler.registerBidiPacket(UpdateWatchedMeteor::class.java, UpdateWatchedMeteor.Handler())
        packetHandler.registerBidiPacket(SyncSpell::class.java, SyncSpell.Handler())
        packetHandler.registerBidiPacket(SyncClearSpells::class.java, SyncClearSpells.Handler())
        packetHandler.registerPacket(SyncAnimation::class.java, SyncAnimation.Handler(), Side.CLIENT)
        packetHandler.registerPacket(SyncItemWithCooldown::class.java, SyncItemWithCooldown.Handler(), Side.CLIENT)
        packetHandler.registerPacket(SyncVoidChest::class.java, SyncVoidChest.Handler(), Side.CLIENT)
        packetHandler.registerPacket(SyncParticle::class.java, SyncParticle.Handler(), Side.CLIENT)
        packetHandler.registerPacket(SyncFreezeData::class.java, SyncFreezeData.Handler(), Side.CLIENT)
        packetHandler.registerPacket(PlayEnariasFightMusic::class.java, PlayEnariasFightMusic.Handler(), Side.CLIENT)
        packetHandler.registerPacket(FireWristCrossbow::class.java, FireWristCrossbow.Handler(), Side.SERVER)
        packetHandler.registerPacket(ProcessSextantInput::class.java, ProcessSextantInput.Handler(), Side.SERVER)
        packetHandler.registerPacket(
            SyncSelectedWristCrossbowBolt::class.java,
            SyncSelectedWristCrossbowBolt.Handler(),
            Side.SERVER
        )
        packetHandler.registerPacket(SyncSpellKeyPress::class.java, SyncSpellKeyPress.Handler(), Side.SERVER)
    }
}