package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemWithPerItemCooldown
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Packet used to update the client of the server-client time difference used to show item cooldowns
 *
 * @property timeServer The time it is on the server
 * @property itemToSync The item to synchronize
 */
class SyncItemWithCooldown : IMessage {
    private var timeServer: Long
    private lateinit var itemToSync: AOTDItemWithPerItemCooldown

    /**
     * Unused by required default constructor
     */
    constructor() {
        timeServer = -1
    }

    /**
     * Constructor initializes fields
     *
     * @param timeServer The time of the server
     * @param itemToSync The item to syncronize
     */
    constructor(timeServer: Long, itemToSync: AOTDItemWithPerItemCooldown) {
        this.timeServer = timeServer
        this.itemToSync = itemToSync
    }

    /**
     * Reads the packet information from the raw bytes
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf) {
        timeServer = buf.readLong()
        itemToSync = Item.getItemById(buf.readInt()) as AOTDItemWithPerItemCooldown
    }

    /**
     * Writes the packet information into the byte buffer
     *
     * @param buf The buffer to write into
     */
    override fun toBytes(buf: ByteBuf) {
        buf.writeLong(timeServer)
        buf.writeInt(Item.getIdFromItem(itemToSync))
    }

    /**
     * Handler handles SyncCooldown packets on the client side
     */
    class Handler : MessageHandler.Client<SyncItemWithCooldown>() {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg          the message received
         * @param ctx          the message context object. This contains additional information about the packet.
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncItemWithCooldown, ctx: MessageContext) {
            // Compute the difference between client and server time and store that info
            msg.itemToSync.updateServerClientDifference(System.currentTimeMillis() - msg.timeServer)
        }
    }
}