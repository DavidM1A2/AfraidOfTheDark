package com.davidm1a2.afraidofthedark.common.packets.otherPackets;

import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import com.davidm1a2.afraidofthedark.common.spell.Spell;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Packet sent from client to server to tell the server that a spell keybinding was pressed
 */
public class SyncSpellKeyPress implements IMessage
{
    // The name of the key that was pressed
    private String keyPressedName;

    /**
     * Default constructor is required for reflection but not used
     */
    public SyncSpellKeyPress()
    {
        this.keyPressedName = null;
    }

    /**
     * Overloaded constructor sets the key that was pressed
     *
     * @param keyPressedName The name of the key that was pressed
     */
    public SyncSpellKeyPress(String keyPressedName)
    {
        this.keyPressedName = keyPressedName;
    }

    /**
     * Writes the contents of the packet to the byte buffer
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(final ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, keyPressedName);
    }

    /**
     * Reads in the state of the packet from the byte buffer
     *
     * @param buf The raw bytes to read from
     */
    @Override
    public void fromBytes(final ByteBuf buf)
    {
        this.keyPressedName = ByteBufUtils.readUTF8String(buf);
    }

    /**
     * Static handler class used to handle the spell key press packet
     */
    public static class Handler extends MessageHandler.Server<SyncSpellKeyPress>
    {
        /**
         * Handles the message on the server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    The context that the message was sent through
         */
        @Override
        public void handleServerMessage(EntityPlayer player, SyncSpellKeyPress msg, MessageContext ctx)
        {
            // Grab the player's spell manager
            IAOTDPlayerSpellManager spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
            // Ensure the keybinding exists
            if (spellManager.keybindExists(msg.keyPressedName))
            {
                // Grab the spell to fire, and cast it
                Spell spellToFire = spellManager.getSpellForKeybinding(msg.keyPressedName);
                spellToFire.attemptToCast(player);
            }
        }
    }
}
