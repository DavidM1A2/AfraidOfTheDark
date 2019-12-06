package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler.Bidirectional
import com.davidm1a2.afraidofthedark.common.spell.Spell
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * This is a packet that is sent to server or client to ensure a given spell is saved in the spell manager
 *
 * @property spell The spell to sync
 * @property keybind The keybinding bound to the spell
 */
class SyncSpell : IMessage
{
    private lateinit var spell: Spell
    private var keybind: String?

    /**
     * Required default constructor that is not used
     */
    constructor()
    {
        keybind = null
    }

    /**
     * Constructor that initializes the fields
     *
     * @param spell The spell to sync
     * @param keybind The keybind bound to the spell or null if no such keybind exists
     */
    constructor(spell: Spell, keybind: String?)
    {
        this.spell = spell
        this.keybind = keybind
    }

    /**
     * Converts the java objects into a byte buf
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        // First write if we have a keybind
        val hasKeybind = keybind != null
        buf.writeBoolean(hasKeybind)

        // Then write the keybind out
        if (hasKeybind)
        {
            ByteBufUtils.writeUTF8String(buf, keybind)
        }

        // Finally write the spell NBT
        ByteBufUtils.writeTag(buf, spell.serializeNBT())
    }

    /**
     * Converts the byte buf into the java objects
     *
     * @param buf The buffer to read
     */
    override fun fromBytes(buf: ByteBuf)
    {
        // First test if we have a keybind
        val hasKeybind = buf.readBoolean()

        // If we have a keybind read it in
        if (hasKeybind)
        {
            keybind = ByteBufUtils.readUTF8String(buf)
        }

        // Finally read in the spell's NBT
        spell = Spell(ByteBufUtils.readTag(buf))
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    class Handler : Bidirectional<SyncSpell>()
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncSpell, ctx: MessageContext)
        {
            val spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)!!

            // Add the spell and keybind it if necessary
            spellManager.addOrUpdateSpell(msg.spell)
            if (msg.keybind != null)
            {
                spellManager.keybindSpell(msg.keybind!!, msg.spell)
            }
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleServerMessage(player: EntityPlayer, msg: SyncSpell, ctx: MessageContext)
        {
            val spellManager = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)!!

            // Add the spell and keybind it if necessary
            spellManager.addOrUpdateSpell(msg.spell)
            if (msg.keybind != null)
            {
                spellManager.keybindSpell(msg.keybind!!, msg.spell)
            }
        }
    }
}