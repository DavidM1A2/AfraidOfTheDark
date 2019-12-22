package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler.Bidirectional
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import org.apache.commons.lang3.StringUtils

/**
 * Packet that can be sent from client -> server to tell the server to make a meteor for us, and server -> client
 * to tell the client what meteor lat/long/drop_angle/type they are watching
 *
 * @property meteorEntry The meteor entry that the user saw
 * @property dropAngle The first field we need to figure out where the meteor landed
 * @property latitude The second field we need to figure out where the meteor landed
 * @property longitude The third field we need to figure out where the meteor landed
 */
class UpdateWatchedMeteor : IMessage
{
    private var meteorEntry: MeteorEntry?
    private var dropAngle: Int
    private var latitude: Int
    private var longitude: Int

    /**
     * Default constructor is required but not used
     */
    constructor()
    {
        meteorEntry = null
        dropAngle = 0
        latitude = 0
        longitude = 0
    }

    /**
     * Constructor which will be used by
     * 1. The server to tell the client what the properties of the watched meteor are
     * 2. The client to tell the server what meteor was selected in the telescope GUI
     *
     * @param meteorEntry The meteor being watched
     * @param dropAngle   The angle the meteor dropped at
     * @param latitude    The latitude the meteor was at
     * @param longitude   The longitude the meteor was at
     */
    constructor(meteorEntry: MeteorEntry?, dropAngle: Int = 0, latitude: Int = 0, longitude: Int = 0)
    {
        this.meteorEntry = meteorEntry
        this.dropAngle = dropAngle
        this.latitude = latitude
        this.longitude = longitude
    }

    /**
     * Converts the byte buffer to a structured format
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf)
    {
        val meteorEntryString = ByteBufUtils.readUTF8String(buf)
        meteorEntry = if (StringUtils.equals(meteorEntryString, "none"))
        {
            null
        }
        else
        {
            ModRegistries.METEORS.getValue(ResourceLocation(meteorEntryString))
        }

        dropAngle = buf.readInt()
        latitude = buf.readInt()
        longitude = buf.readInt()
    }

    /**
     * Converts the structured format into a byte buffer
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        ByteBufUtils.writeUTF8String(buf, meteorEntry?.registryName?.toString() ?: "none")
        buf.writeInt(dropAngle)
        buf.writeInt(latitude)
        buf.writeInt(longitude)
    }

    /**
     * Handler handles UpdateWatchedMeteor packets on both sides. Client updates capabilities and server
     * updates capabilities and updates the client too
     */
    class Handler : Bidirectional<UpdateWatchedMeteor>()
    {
        /**
         * When the client gets the message update the player's watched meteor capability
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent through
         */
        override fun handleClientMessage(player: EntityPlayer, msg: UpdateWatchedMeteor, ctx: MessageContext)
        {
            // Update the player's watched meteor
            player.getBasics().setWatchedMeteor(msg.meteorEntry, msg.dropAngle, msg.latitude, msg.longitude)
        }

        /**
         * When the server gets the message randomly generate 3 meteor fields and send that data
         * back to the client
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent through
         */
        override fun handleServerMessage(player: EntityPlayer, msg: UpdateWatchedMeteor, ctx: MessageContext)
        {
            // Randomize the meteor drop angle, latitude, and longitude
            val watchedMeteor = msg.meteorEntry
            val dropAngle = player.rng.nextInt(45) + 5
            val latitude = player.rng.nextInt(50) + 5
            val longitude = player.rng.nextInt(130) + 5

            // Tell the player about the meteor estimated values
            player.sendMessage(TextComponentTranslation("aotd.falling_meteor.info.header", TextComponentTranslation(watchedMeteor!!.getUnLocalizedName())))
            player.sendMessage(TextComponentTranslation("aotd.falling_meteor.info.data", dropAngle, latitude, longitude))
            player.sendMessage(TextComponentTranslation("aotd.falling_meteor.info.help"))

            // Update the player's watched meteor and send them values
            val playerBasics = player.getBasics()
            playerBasics.setWatchedMeteor(watchedMeteor, dropAngle, latitude, longitude)
            playerBasics.syncWatchedMeteor(player)
        }
    }
}