package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler.Bidirectional
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Packet used to sync research between server and client
 */
class SyncResearch : IMessage
{
    private var researchToUnlocked: MutableMap<Research, Boolean>
    private var notifyNewResearch: Boolean

    constructor()
    {
        researchToUnlocked = mutableMapOf()
        notifyNewResearch = false
    }

    constructor(researchToUnlocked: MutableMap<Research, Boolean>, notifyNewResearch: Boolean = false)
    {
        this.researchToUnlocked = researchToUnlocked
        this.notifyNewResearch = notifyNewResearch
    }

    /**
     * Converts from the byte buffer into the structured research map
     *
     * @param buf The buffer to read
     */
    override fun fromBytes(buf: ByteBuf)
    {
        // Read the notify flag first
        notifyNewResearch = buf.readBoolean()

        // Read the compound from the buffer
        val data = ByteBufUtils.readTag(buf)!!

        // For each research read our compound to test if it is researched or not
        for (research in ModRegistries.RESEARCH)
        {
            researchToUnlocked[research] = data.getBoolean(research.registryName.toString())
        }
    }

    /**
     * Writes the structured research map into a byte buffer
     *
     * @param buf The buffer to read
     */
    override fun toBytes(buf: ByteBuf)
    {
        // Write the notify flag first
        buf.writeBoolean(notifyNewResearch)
        // Create a compound to write to
        val data = NBTTagCompound()

        // For each research write a boolean if that research is researched
        researchToUnlocked.forEach()
        { (research, researched) ->
            data.setBoolean(
                    research.registryName.toString(),
                    researched
            )
        }

        // Write the compound
        ByteBufUtils.writeTag(buf, data)
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    class Handler : Bidirectional<SyncResearch>()
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncResearch, ctx: MessageContext)
        {
            // Grab the player's current research, we must use Minecraft.getMinecraft().player because the player passed to use might be null
            val playerResearch = player.getResearch()

            // Iterate over the new research
            msg.researchToUnlocked.forEach()
            { (research, researched) ->
                // If the research was not researched and it now is researched show the popup
                val wasResearched = playerResearch.isResearched(research)
                val showPopup = researched && !wasResearched && msg.notifyNewResearch

                // Set the research
                if (showPopup)
                {
                    playerResearch.setResearchAndAlert(research, researched, player)
                }
                else
                {
                    playerResearch.setResearch(research, researched)
                }
            }
        }

        /**
         * Handles the packet on server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleServerMessage(player: EntityPlayer, msg: SyncResearch, ctx: MessageContext)
        {
            // Grab the player's current research
            val playerResearch = player.getResearch()

            // Update each research, don't show popups server side
            msg.researchToUnlocked.forEach()
            { (research, researched) ->
                playerResearch.setResearch(
                        research,
                        researched
                )
            }
        }
    }
}