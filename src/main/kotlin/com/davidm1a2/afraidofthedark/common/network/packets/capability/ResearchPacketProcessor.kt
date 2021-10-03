package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.time.Instant
import java.time.ZonedDateTime

/**
 * Packet used to sync research between server and client
 */
class ResearchPacketProcessor(private val researchOverlayHandler: ResearchOverlayHandler) : PacketProcessor<ResearchPacket> {
    override fun encode(msg: ResearchPacket, buf: PacketBuffer) {
        // Write the notify flag first
        buf.writeBoolean(msg.notifyNewResearch)

        // Create a compound to write to
        val data = CompoundNBT()
        // For each research write a boolean if that research is researched
        msg.researchToUnlocked.forEach { (research, researchDate) ->
            if (researchDate != null) {
                data.putLong(research.registryName.toString(), researchDate.toInstant().toEpochMilli())
            }
        }

        // Write the compound
        buf.writeNbt(data)
    }

    override fun decode(buf: PacketBuffer): ResearchPacket {
        // Read the notify flag first
        val notifyNewResearch = buf.readBoolean()

        // Read the compound from the buffer
        val data = buf.readNbt()!!

        return ResearchPacket(
            ModRegistries.RESEARCH.associateWith {
                if (data.contains(it.registryName.toString())) {
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(data.getLong(it.registryName.toString())), Constants.DEFAULT_TIME_ZONE)
                } else {
                    null
                }
            },
            notifyNewResearch
        )
    }

    override fun process(msg: ResearchPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!

            // Grab the player's current research, we must use Minecraft.getMinecraft().player because the player passed to use might be null
            val playerResearch = player.getResearch()

            // Iterate over the new research
            msg.researchToUnlocked.forEach { (research, newResearchDate) ->
                // If the research was not researched and it now is researched show the popup
                val wasResearched = playerResearch.isResearched(research)
                val showPopup = newResearchDate != null && !wasResearched && msg.notifyNewResearch

                // Set the research
                playerResearch.setResearch(research, newResearchDate)
                if (showPopup) {
                    researchOverlayHandler.displayResearch(research)
                }
            }
        }
    }
}