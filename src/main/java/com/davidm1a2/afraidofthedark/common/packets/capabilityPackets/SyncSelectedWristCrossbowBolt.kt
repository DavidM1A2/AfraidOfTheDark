package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Packet for the client to tell the server what bolt type they currently have selected
 *
 * @property selectedWristCrossbowBoltIndex The selected wrist crossbow bolt index to update
 */
class SyncSelectedWristCrossbowBolt : IMessage
{
    private var selectedWristCrossbowBoltIndex: Int

    /**
     * Default constructor is required but not used
     */
    constructor()
    {
        selectedWristCrossbowBoltIndex = 0
    }

    /**
     * Overloaded constructor takes the index of the currently selected bolt index
     *
     * @param index The new bolt index
     */
    constructor(index: Int)
    {
        selectedWristCrossbowBoltIndex = index
    }

    /**
     * Converts the packet from bytes to types
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf)
    {
        selectedWristCrossbowBoltIndex = buf.readInt()
    }

    /**
     * Converts the packet from types to raw bytes
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        buf.writeInt(selectedWristCrossbowBoltIndex)
    }

    /**
     * Handler class handles when we receive packets from the client
     */
    class Handler : MessageHandler.Server<SyncSelectedWristCrossbowBolt>()
    {
        /**
         * Called when we receive a new packet from a client
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    The message's context
         */
        override fun handleServerMessage(player: EntityPlayer, msg: SyncSelectedWristCrossbowBolt, ctx: MessageContext)
        {
            player.getBasics().selectedWristCrossbowBoltIndex = msg.selectedWristCrossbowBoltIndex
        }
    }
}