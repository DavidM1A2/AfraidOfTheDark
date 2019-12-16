package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler.Bidirectional
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * This is a packet that is sent to a client or from a client to the server that updates the status of if the player has begun the mod
 *
 * @property startedAOTD Flag that is true or false depending on if we started the mod or not
 */
class SyncStartedAOTD : IMessage
{
    private var startedAOTD: Boolean

    /**
     * Required default constructor that is not used
     */
    constructor()
    {
        startedAOTD = false
    }

    /**
     * Constructor that initializes the field
     *
     * @param startedAOTD True if the user has started AOTD, false otherwise
     */
    constructor(startedAOTD: Boolean)
    {
        this.startedAOTD = startedAOTD
    }

    /**
     * Converts the byte buf into the boolean data
     *
     * @param buf The buffer to read
     */
    override fun fromBytes(buf: ByteBuf)
    {
        startedAOTD = buf.readBoolean()
    }

    /**
     * Converts the boolean into a byte buf
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        buf.writeBoolean(startedAOTD)
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    class Handler : Bidirectional<SyncStartedAOTD>()
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncStartedAOTD, ctx: MessageContext)
        {
            player.getCapability(ModCapabilities.PLAYER_BASICS, null)!!.startedAOTD = msg.startedAOTD
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleServerMessage(player: EntityPlayer, msg: SyncStartedAOTD, ctx: MessageContext)
        {
            player.getCapability(ModCapabilities.PLAYER_BASICS, null)!!.startedAOTD = msg.startedAOTD
        }
    }
}