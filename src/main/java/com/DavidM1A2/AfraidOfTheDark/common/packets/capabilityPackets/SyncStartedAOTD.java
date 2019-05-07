package com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets;

import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This is a packet that is sent to a client or from a client to the server that updates the status of if the player has begun the mod
 */
public class SyncStartedAOTD implements IMessage
{
    // Flag that is true or false depending on if we started the mod or not
    private boolean startedAOTD;

    /**
     * Required default constructor that is not used
     */
    public SyncStartedAOTD()
    {
        this.startedAOTD = false;
    }

    /**
     * Constructor that initializes the field
     *
     * @param startedAOTD True if the user has started AOTD, false otherwise
     */
    public SyncStartedAOTD(boolean startedAOTD)
    {
        this.startedAOTD = startedAOTD;
    }

    /**
     * Converts the byte buf into the boolean data
     *
     * @param buf The buffer to read
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.startedAOTD = buf.readBoolean();
    }

    /**
     * Converts the boolean into a byte buf
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.startedAOTD);
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    public static class Handler extends MessageHandler.Bidirectional<SyncStartedAOTD>
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleClientMessage(EntityPlayer player, SyncStartedAOTD msg, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() -> player.getCapability(ModCapabilities.PLAYER_BASICS, null).setStartedAOTD(msg.startedAOTD));
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleServerMessage(EntityPlayer player, SyncStartedAOTD msg, MessageContext ctx)
        {
            player.world.getMinecraftServer().addScheduledTask(() -> player.getCapability(ModCapabilities.PLAYER_BASICS, null).setStartedAOTD(msg.startedAOTD));
        }
    }
}
