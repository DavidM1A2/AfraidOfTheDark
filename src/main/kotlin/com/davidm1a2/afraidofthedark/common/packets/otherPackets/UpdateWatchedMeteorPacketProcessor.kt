package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet that can be sent from client -> server to tell the server to make a meteor for us, and server -> client
 * to tell the client what meteor lat/long/drop_angle/type they are watching
 */
class UpdateWatchedMeteorPacketProcessor : PacketProcessor<UpdateWatchedMeteorPacket> {
    override fun encode(msg: UpdateWatchedMeteorPacket, buf: PacketBuffer) {
        buf.writeString(msg.meteorEntry?.registryName?.toString() ?: "none")
        buf.writeInt(msg.accuracy)
        buf.writeInt(msg.dropAngle)
        buf.writeInt(msg.latitude)
        buf.writeInt(msg.longitude)
    }

    override fun decode(buf: PacketBuffer): UpdateWatchedMeteorPacket {
        val meteorEntryString = buf.readString(500)
        val meteorEntry = if (meteorEntryString == "none") {
            null
        } else {
            ModRegistries.METEORS.getValue(ResourceLocation(meteorEntryString))
        }

        return UpdateWatchedMeteorPacket(
            meteorEntry,
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        )
    }

    override fun process(msg: UpdateWatchedMeteorPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            // Update the player's watched meteor
            Minecraft.getInstance().player.getBasics().setWatchedMeteor(msg.meteorEntry, msg.accuracy, msg.dropAngle, msg.latitude, msg.longitude)
        } else if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.sender!!
            // Randomize the meteor drop angle, latitude, and longitude
            val watchedMeteor = msg.meteorEntry
            val accuracy = msg.accuracy
            val dropAngle = player.rng.nextInt(45) + 5
            val latitude = player.rng.nextInt(50) + 5
            val longitude = player.rng.nextInt(130) + 5

            // Tell the player about the meteor estimated values
            player.sendMessage(
                TextComponentTranslation(
                    LocalizationConstants.Sextant.FALLING_METEOR_INFO_HEADER, TextComponentTranslation(watchedMeteor!!.getUnlocalizedName())
                )
            )
            player.sendMessage(TextComponentTranslation(LocalizationConstants.Sextant.FALLING_METEOR_INFO_DATA, dropAngle, latitude, longitude))
            player.sendMessage(TextComponentTranslation(LocalizationConstants.Sextant.FALLING_METEOR_INFO_HELP))

            // Update the player's watched meteor and send them values
            val playerBasics = player.getBasics()
            playerBasics.setWatchedMeteor(watchedMeteor, accuracy, dropAngle, latitude, longitude)
            playerBasics.syncWatchedMeteor(player)
        }
    }
}