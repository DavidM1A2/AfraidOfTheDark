package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerStartedAfraidOfTheDarkEvent
import com.davidm1a2.afraidofthedark.common.network.packets.capability.AOTDPlayerBasicsPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SelectedWristCrossbowBoltPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.StartAOTDPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.UpdateWatchedMeteorPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge

/**
 * Default implementation of the AOTD player basics capability
 *
 * @property selectedWristCrossbowBoltIndex Integer telling us what bolt the wrist crossbow is currently set to fire
 * @property watchedMeteor The meteor the player is currently observing (was last clicked in telescope)
 */
class AOTDPlayerBasics : IAOTDPlayerBasics {
    override var selectedWristCrossbowBoltIndex = 0
    override var watchedMeteor: WatchedMeteor? = null
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

    override fun syncSelectedWristCrossbowBoltIndex(entityPlayer: PlayerEntity) {
        // Can only send this client -> server side
        if (!isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendToServer(
                SelectedWristCrossbowBoltPacket(
                    selectedWristCrossbowBoltIndex
                )
            )
        }
    }

    override fun syncWatchedMeteor(entityPlayer: PlayerEntity) {
        // If we're on server side send the client the meteor data
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(UpdateWatchedMeteorPacket(watchedMeteor), entityPlayer as ServerPlayerEntity)
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