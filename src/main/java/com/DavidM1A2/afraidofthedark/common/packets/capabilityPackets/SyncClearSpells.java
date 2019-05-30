package com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This is a packet that is sent to server or client to clear spells saved in the spell manager
 */
public class SyncClearSpells implements IMessage
{
    /**
     * Required default constructor that is also used since no additional data is sent
     */
    public SyncClearSpells()
    {
    }

    /**
     * Converts the java objects into a byte buf
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    /**
     * Converts the byte buf into the java objects
     *
     * @param buf The buffer to read
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    public static class Handler extends MessageHandler.Bidirectional<SyncClearSpells>
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleClientMessage(EntityPlayer player, SyncClearSpells msg, MessageContext ctx)
        {
            IAOTDPlayerSpellManager spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
            spellManager.clearSpells();
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleServerMessage(EntityPlayer player, SyncClearSpells msg, MessageContext ctx)
        {
            IAOTDPlayerSpellManager spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
            spellManager.clearSpells();
        }
    }
}
