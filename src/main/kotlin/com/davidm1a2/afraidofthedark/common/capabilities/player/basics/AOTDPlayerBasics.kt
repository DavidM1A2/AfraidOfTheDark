package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerStartedAfraidOfTheDarkEvent
import com.davidm1a2.afraidofthedark.common.network.packets.capability.AOTDPlayerBasicsPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.StartAOTDPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.UpdateSelectedPowerSourcePacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.UpdateWatchedMeteorPacket
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge

/**
 * Default implementation of the AOTD player basics capability
 *
 * @property watchedMeteor The meteor the player is currently observing (was last clicked in telescope)
 */
class AOTDPlayerBasics : IAOTDPlayerBasics {
    override var watchedMeteor: WatchedMeteor? = null
    override var selectedPowerSource: SpellPowerSource<*> = ModSpellPowerSources.CREATIVE
    private val multiplicities = mutableMapOf<ResourceLocation, Int>()

    private fun isServerSide(entityPlayer: PlayerEntity): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun startAOTD(entityPlayer: PlayerEntity) {
        if (isServerSide(entityPlayer)) {
            throw IllegalStateException("Only a client can make a player start AOTD")
        }

        MinecraftForge.EVENT_BUS.post(PlayerStartedAfraidOfTheDarkEvent(entityPlayer))
        AfraidOfTheDark.packetHandler.sendToServer(StartAOTDPacket())
    }

    override fun setMultiplicity(key: ResourceLocation, value: Int) {
        multiplicities[key] = value
    }

    override fun getMultiplicity(key: ResourceLocation): Int {
        return multiplicities[key] ?: 0
    }

    override fun listMultiplicities(): List<ResourceLocation> {
        return multiplicities.keys.toList()
    }

    override fun syncWatchedMeteor(entityPlayer: PlayerEntity) {
        // If we're on server side send the client the meteor data
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(UpdateWatchedMeteorPacket(watchedMeteor), entityPlayer as ServerPlayerEntity)
        }
    }

    override fun syncSelectedPowerSource(entityPlayer: PlayerEntity) {
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(UpdateSelectedPowerSourcePacket(selectedPowerSource), entityPlayer as ServerPlayerEntity)
        } else {
            AfraidOfTheDark.packetHandler.sendToServer(UpdateSelectedPowerSourcePacket(selectedPowerSource))
        }
    }

    override fun syncAll(entityPlayer: PlayerEntity) {
        // If we're on server side send the player's data
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(AOTDPlayerBasicsPacket(this), entityPlayer as ServerPlayerEntity)
        } else {
            AfraidOfTheDark.packetHandler.sendToServer(AOTDPlayerBasicsPacket(this))
        }
    }
}