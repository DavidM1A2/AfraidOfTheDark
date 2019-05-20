package com.DavidM1A2.afraidofthedark.common.packets.otherPackets;

import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItemWithPerItemCooldown;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Packet used to update the client of the server-client time difference used to show item cooldowns
 */
public class SyncItemWithCooldown implements IMessage
{
    // The time it is on the server
    private long timeServer;
    // The item to syncronize
    private AOTDItemWithPerItemCooldown itemToSync;

    /**
     * Unused by required default constructor
     */
    public SyncItemWithCooldown()
    {
        this.timeServer = -1;
        this.itemToSync = null;
    }

    /**
     * Constructor initializes fields
     *
     * @param timeServer The time of the server
     * @param itemToSync The item to syncronize
     */
    public SyncItemWithCooldown(long timeServer, AOTDItemWithPerItemCooldown itemToSync)
    {
        this.timeServer = timeServer;
        this.itemToSync = itemToSync;
    }

    /**
     * Reads the packet information from the raw bytes
     *
     * @param buf The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.timeServer = buf.readLong();
        this.itemToSync = (AOTDItemWithPerItemCooldown) Item.getItemById(buf.readInt());
    }

    /**
     * Writes the packet information into the byte buffer
     *
     * @param buf The buffer to write into
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(this.timeServer);
        buf.writeInt(Item.getIdFromItem(this.itemToSync));
    }

    /**
     * Handler handles SyncCooldown packets on the client side
     */
    public static class Handler extends MessageHandler.Client<SyncItemWithCooldown>
    {
        /**
         * Handles the packet on client side
         *
         * @param entityPlayer the player reference (the player who received the packet)
         * @param msg          the message received
         * @param ctx          the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleClientMessage(EntityPlayer entityPlayer, SyncItemWithCooldown msg, MessageContext ctx)
        {
            // Compute the difference between client and server time and store that info
            msg.itemToSync.updateServerClientDifference(System.currentTimeMillis() - msg.timeServer);
        }
    }
}
