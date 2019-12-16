package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler.Bidirectional
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * This is a packet that is sent to a client or from a client to the server that updates the status of all player basics
 *
 * @property data The NBT data to send
 */
class SyncAOTDPlayerBasics : IMessage
{
    private lateinit var data: NBTTagCompound

    /**
     * Default constructor is required but not used
     */
    constructor()

    /**
     * Overloaded constructor expects a capability to send
     *
     * @param playerBasics The player basics capability to send
     */
    constructor(playerBasics: IAOTDPlayerBasics)
    {
        data = ModCapabilities.PLAYER_BASICS.storage.writeNBT(ModCapabilities.PLAYER_BASICS, playerBasics, null) as NBTTagCompound
    }

    /**
     * Reads a NBTTagCompound from the buffer
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf)
    {
        data = ByteBufUtils.readTag(buf)!!
    }

    /**
     * Writes an NBTTagCompound to the buffer
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        ByteBufUtils.writeTag(buf, data)
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    class Handler : Bidirectional<SyncAOTDPlayerBasics>()
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncAOTDPlayerBasics, ctx: MessageContext)
        {
            // Grab the current player's capabilities
            val playerBasics = Minecraft.getMinecraft().player.getCapability(ModCapabilities.PLAYER_BASICS, null)
            // Read the new capabilities into the player's data
            ModCapabilities.PLAYER_BASICS.storage.readNBT(ModCapabilities.PLAYER_BASICS, playerBasics, null, msg.data)
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleServerMessage(player: EntityPlayer, msg: SyncAOTDPlayerBasics, ctx: MessageContext)
        {
            // Send the player his/her current capabilities in a packet as requested
            player.getCapability(ModCapabilities.PLAYER_BASICS, null)!!.syncAll(player)
        }
    }
}