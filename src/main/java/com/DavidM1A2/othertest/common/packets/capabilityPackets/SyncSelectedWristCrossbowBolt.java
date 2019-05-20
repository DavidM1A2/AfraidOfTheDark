package com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets;

import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Packet for the client to tell the server what bolt type they currently have selected
 */
public class SyncSelectedWristCrossbowBolt implements IMessage
{
    // The selected wrist crossbow bolt index to update
    private int selectedWristCrossbowBoltIndex;

    /**
     * Default constructor is required but not used
     */
    public SyncSelectedWristCrossbowBolt()
    {
        this.selectedWristCrossbowBoltIndex = 0;
    }

    /**
     * Overloaded constructor takes the index of the currently selected bolt index
     *
     * @param index The new bolt index
     */
    public SyncSelectedWristCrossbowBolt(int index)
    {
        this.selectedWristCrossbowBoltIndex = index;
    }

    /**
     * Converts the packet from bytes to types
     *
     * @param buf The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.selectedWristCrossbowBoltIndex = buf.readInt();
    }

    /**
     * Converts the packet from types to raw bytes
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.selectedWristCrossbowBoltIndex);
    }

    /**
     * Handler class handles when we receive packets from the client
     */
    public static class Handler extends MessageHandler.Server<SyncSelectedWristCrossbowBolt>
    {
        /**
         * Called when we receive a new packet from a client
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    The message's context
         */
        @Override
        public void handleServerMessage(EntityPlayer player, SyncSelectedWristCrossbowBolt msg, MessageContext ctx)
        {
            player.getCapability(ModCapabilities.PLAYER_BASICS, null).setSelectedWristCrossbowBoltIndex(msg.selectedWristCrossbowBoltIndex);
        }
    }
}
