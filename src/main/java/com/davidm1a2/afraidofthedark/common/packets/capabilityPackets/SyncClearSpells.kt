package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler.Bidirectional
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * This is a packet that is sent to server or client to clear spells saved in the spell manager
 *
 * @constructor Required default constructor that is also used since no additional data is sent
 */
class SyncClearSpells : IMessage
{
    /**
     * Converts the java objects into a byte buf
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
    }

    /**
     * Converts the byte buf into the java objects
     *
     * @param buf The buffer to read
     */
    override fun fromBytes(buf: ByteBuf)
    {
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    class Handler : Bidirectional<SyncClearSpells>()
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncClearSpells, ctx: MessageContext)
        {
            val spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)!!
            spellManager.clearSpells()
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleServerMessage(player: EntityPlayer, msg: SyncClearSpells, ctx: MessageContext)
        {
            val spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)!!
            spellManager.clearSpells()
        }
    }
}