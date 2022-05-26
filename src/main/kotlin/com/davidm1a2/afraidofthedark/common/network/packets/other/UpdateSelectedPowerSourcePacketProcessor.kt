package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet that can be sent to tell the server or client what power source the player has selected
 */
class UpdateSelectedPowerSourcePacketProcessor : PacketProcessor<UpdateSelectedPowerSourcePacket> {
    override fun encode(msg: UpdateSelectedPowerSourcePacket, buf: PacketBuffer) {
        buf.writeUtf(msg.selectedPowerSource.registryName!!.toString())
    }

    override fun decode(buf: PacketBuffer): UpdateSelectedPowerSourcePacket {
        val selectedPowerSourceRegistryName = ResourceLocation(buf.readUtf())
        val selectedPowerSource = ModRegistries.SPELL_POWER_SOURCES.getValue(selectedPowerSourceRegistryName)
        return if (selectedPowerSource == null) {
            UpdateSelectedPowerSourcePacket(ModSpellPowerSources.CREATIVE)
        } else {
            UpdateSelectedPowerSourcePacket(selectedPowerSource)
        }
    }

    override fun process(msg: UpdateSelectedPowerSourcePacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            Minecraft.getInstance().player!!.getBasics().selectedPowerSource = msg.selectedPowerSource
        } else if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.sender!!
            val selectedPowerSource = msg.selectedPowerSource
            val prerequisiteResearch = selectedPowerSource.prerequisiteResearch

            if (prerequisiteResearch == null || player.getResearch().isResearched(prerequisiteResearch)) {
                player.getBasics().selectedPowerSource = msg.selectedPowerSource
            }
        }
    }
}