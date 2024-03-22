package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.WatchedMeteor
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslatableComponent
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet that can be sent from client -> server to tell the server to make a meteor for us, and server -> client
 * to tell the client what meteor lat/long/drop_angle/type they are watching
 */
class UpdateWatchedMeteorPacketProcessor : PacketProcessor<UpdateWatchedMeteorPacket> {
    override fun encode(msg: UpdateWatchedMeteorPacket, buf: PacketBuffer) {
        if (msg.watchedMeteor != null) {
            val meteor = msg.watchedMeteor
            buf.writeUtf(meteor.meteor.registryName.toString())
            buf.writeInt(meteor.accuracy)
            buf.writeInt(meteor.dropAngle)
            buf.writeInt(meteor.latitude)
            buf.writeInt(meteor.longitude)
        } else {
            buf.writeUtf("none")
        }
    }

    override fun decode(buf: PacketBuffer): UpdateWatchedMeteorPacket {
        val meteorEntryString = buf.readUtf()
        val meteorEntry = if (meteorEntryString == "none") {
            null
        } else {
            WatchedMeteor(ModRegistries.METEORS.getValue(ResourceLocation(meteorEntryString))!!, buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt())
        }

        return UpdateWatchedMeteorPacket(meteorEntry)
    }

    override fun process(msg: UpdateWatchedMeteorPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            // Update the player's watched meteor
            Minecraft.getInstance().player!!.getBasics().watchedMeteor = msg.watchedMeteor
        } else if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.sender!!
            // Randomize the meteor drop angle, latitude, and longitude
            val watchedMeteor = msg.watchedMeteor!!.meteor
            val accuracy = msg.watchedMeteor.accuracy
            val dropAngle = player.random.nextInt(45) + 5
            val latitude = player.random.nextInt(50) + 5
            val longitude = player.random.nextInt(130) + 5

            // Tell the player about the meteor estimated values
            player.sendMessage(TranslatableComponent("message.afraidofthedark.falling_meteor.info.header", watchedMeteor.getName()))
            player.sendMessage(TranslatableComponent("message.afraidofthedark.falling_meteor.info.data", dropAngle, latitude, longitude))
            player.sendMessage(TranslatableComponent("message.afraidofthedark.falling_meteor.info.help"))

            if (player.getResearch().isResearched(watchedMeteor.prerequisiteResearch)) {
                // Update the player's watched meteor and send them values
                val playerBasics = player.getBasics()
                playerBasics.watchedMeteor = WatchedMeteor(watchedMeteor, accuracy, dropAngle, latitude, longitude)
                playerBasics.syncWatchedMeteor(player)
            }
        }
    }
}