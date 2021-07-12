package com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet used to sync research between server and client
 */
class ResearchPacketProcessor : PacketProcessor<ResearchPacket> {
    override fun encode(msg: ResearchPacket, buf: PacketBuffer) {
        // Write the notify flag first
        buf.writeBoolean(msg.notifyNewResearch)

        // Create a compound to write to
        val data = CompoundNBT()
        // For each research write a boolean if that research is researched
        msg.researchToUnlocked.forEach { (research, researched) ->
            data.putBoolean(research.registryName.toString(), researched)
        }

        // Write the compound
        buf.writeCompoundTag(data)
    }

    override fun decode(buf: PacketBuffer): ResearchPacket {
        // Read the notify flag first
        val notifyNewResearch = buf.readBoolean()

        // Read the compound from the buffer
        val data = buf.readCompoundTag()!!

        return ResearchPacket(
            // For each research read our compound to test if it is researched or not
            ModRegistries.RESEARCH.map { it to data.getBoolean(it.registryName.toString()) }.toMap(),
            notifyNewResearch
        )
    }

    override fun process(msg: ResearchPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!

            // Grab the player's current research, we must use Minecraft.getMinecraft().player because the player passed to use might be null
            val playerResearch = player.getResearch()

            // Iterate over the new research
            msg.researchToUnlocked.forEach { (research, researched) ->
                // If the research was not researched and it now is researched show the popup
                val wasResearched = playerResearch.isResearched(research)
                val showPopup = researched && !wasResearched && msg.notifyNewResearch

                // Set the research
                if (showPopup) {
                    playerResearch.setResearchAndAlert(research, researched, player)
                } else {
                    playerResearch.setResearch(research, researched)
                }
            }
        } else if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            // Grab the player's current research
            val playerResearch = ctx.sender!!.getResearch()

            // Update each research, don't show popups server side
            msg.researchToUnlocked.forEach { (research, researched) ->
                playerResearch.setResearch(
                    research,
                    researched
                )
            }
        }
    }
}