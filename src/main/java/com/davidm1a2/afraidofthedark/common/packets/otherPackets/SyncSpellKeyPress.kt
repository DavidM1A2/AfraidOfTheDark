package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Packet sent from client to server to tell the server that a spell keybinding was pressed
 *
 * @property keyPressedName The name of the key that was pressed
 */
class SyncSpellKeyPress : IMessage {
    private lateinit var keyPressedName: String

    /**
     * Default constructor is required for reflection but not used
     */
    constructor()

    /**
     * Overloaded constructor sets the key that was pressed
     *
     * @param keyPressedName The name of the key that was pressed
     */
    constructor(keyPressedName: String) {
        this.keyPressedName = keyPressedName
    }

    /**
     * Writes the contents of the packet to the byte buffer
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf) {
        ByteBufUtils.writeUTF8String(buf, keyPressedName)
    }

    /**
     * Reads in the state of the packet from the byte buffer
     *
     * @param buf The raw bytes to read from
     */
    override fun fromBytes(buf: ByteBuf) {
        keyPressedName = ByteBufUtils.readUTF8String(buf)
    }

    /**
     * Static handler class used to handle the spell key press packet
     */
    class Handler : MessageHandler.Server<SyncSpellKeyPress>() {
        /**
         * Handles the message on the server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    The context that the message was sent through
         */
        override fun handleServerMessage(player: EntityPlayer, msg: SyncSpellKeyPress, ctx: MessageContext) {
            // Grab the player's spell manager
            val spellManager = player.getSpellManager()

            // Ensure the keybinding exists
            if (spellManager.keybindExists(msg.keyPressedName)) {
                // Grab the spell to fire, and cast it
                val spellToFire = spellManager.getSpellForKeybinding(msg.keyPressedName)
                spellToFire!!.attemptToCast(player)
            }
        }
    }
}