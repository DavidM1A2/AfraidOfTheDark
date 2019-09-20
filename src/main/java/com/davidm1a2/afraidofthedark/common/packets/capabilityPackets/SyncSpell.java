package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets;

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
 * This is a packet that is sent to server or client to ensure a given spell is saved in the spell manager
 */
public class SyncSpell implements IMessage
{
    // The spell to sync
    private Spell spell;
    // The keybinding bound to the spell
    private String keybind;

    /**
     * Required default constructor that is not used
     */
    public SyncSpell()
    {
        this.spell = null;
        this.keybind = null;
    }

    /**
     * Constructor that initializes the fields
     *
     * @param spell The spell to sync
     * @param keybind The keybind bound to the spell or null if no such keybind exists
     */
    public SyncSpell(Spell spell, String keybind)
    {
        this.spell = spell;
        this.keybind = keybind;
    }

    /**
     * Converts the java objects into a byte buf
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        // First write if we have a keybind
        boolean hasKeybind = this.keybind != null;
        buf.writeBoolean(hasKeybind);
        // Then write the keybind out
        if (hasKeybind)
        {
            ByteBufUtils.writeUTF8String(buf, this.keybind);
        }
        // Finally write the spell NBT
        ByteBufUtils.writeTag(buf, this.spell.serializeNBT());
    }

    /**
     * Converts the byte buf into the java objects
     *
     * @param buf The buffer to read
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        // First test if we have a keybind
        boolean hasKeybind = buf.readBoolean();
        // If we have a keybind read it in
        if (hasKeybind)
        {
            this.keybind = ByteBufUtils.readUTF8String(buf);
        }
        // Finally read in the spell's NBT
        this.spell = new Spell(ByteBufUtils.readTag(buf));
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    public static class Handler extends MessageHandler.Bidirectional<SyncSpell>
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleClientMessage(EntityPlayer player, SyncSpell msg, MessageContext ctx)
        {
            IAOTDPlayerSpellManager spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
            // Add the spell and keybind it if necessary
            spellManager.addOrUpdateSpell(msg.spell);
            if (msg.keybind != null)
            {
                spellManager.keybindSpell(msg.keybind, msg.spell);
            }
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleServerMessage(EntityPlayer player, SyncSpell msg, MessageContext ctx)
        {
            IAOTDPlayerSpellManager spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
            // Add the spell and keybind it if necessary
            spellManager.addOrUpdateSpell(msg.spell);
            if (msg.keybind != null)
            {
                spellManager.keybindSpell(msg.keybind, msg.spell);
            }
        }
    }
}
