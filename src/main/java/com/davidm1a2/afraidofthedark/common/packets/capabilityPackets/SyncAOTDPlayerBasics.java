package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets;

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This is a packet that is sent to a client or from a client to the server that updates the status of all player basics
 */
public class SyncAOTDPlayerBasics implements IMessage
{
    private NBTTagCompound data;

    /**
     * Default constructor is required but not used
     */
    public SyncAOTDPlayerBasics()
    {
        this.data = null;
    }

    /**
     * Overloaded constructor expects a capability to send
     *
     * @param playerBasics The player basics capability to send
     */
    public SyncAOTDPlayerBasics(IAOTDPlayerBasics playerBasics)
    {
        this.data = (NBTTagCompound) ModCapabilities.PLAYER_BASICS.getStorage().writeNBT(ModCapabilities.PLAYER_BASICS, playerBasics, null);
    }

    /**
     * Reads a NBTTagCompound from the buffer
     *
     * @param buf The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.data = ByteBufUtils.readTag(buf);
    }

    /**
     * Writes an NBTTagCompound to the buffer
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.data);
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    public static class Handler extends MessageHandler.Bidirectional<SyncAOTDPlayerBasics>
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleClientMessage(EntityPlayer player, SyncAOTDPlayerBasics msg, MessageContext ctx)
        {
            // Grab the current player's capabilities
            IAOTDPlayerBasics playerBasics = Minecraft.getMinecraft().player.getCapability(ModCapabilities.PLAYER_BASICS, null);
            // Read the new capabilities into the player's data
            ModCapabilities.PLAYER_BASICS.getStorage().readNBT(ModCapabilities.PLAYER_BASICS, playerBasics, null, msg.data);
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleServerMessage(EntityPlayer player, SyncAOTDPlayerBasics msg, MessageContext ctx)
        {
            // Send the player his/her current capabilities in a packet as requested
            player.world.getMinecraftServer().addScheduledTask(() -> player.getCapability(ModCapabilities.PLAYER_BASICS, null).syncAll(player));
        }
    }
}
