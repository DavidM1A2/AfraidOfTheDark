package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.world.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslatableComponent
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.time.ZonedDateTime

class CheatSheetUnlockPacketProcessor : PacketProcessor<CheatSheetUnlockPacket> {
    override fun encode(msg: CheatSheetUnlockPacket, buf: PacketBuffer) {
        buf.writeUtf(msg.research.registryName.toString())
    }

    override fun decode(buf: PacketBuffer): CheatSheetUnlockPacket {
        return CheatSheetUnlockPacket(ModRegistries.RESEARCH.getValue(ResourceLocation(buf.readUtf()))!!)
    }

    override fun process(msg: CheatSheetUnlockPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.sender!!
            if (player.inventory.contains(CHEAT_SHEET)) {
                val playerResearch = player.getResearch()
                if (playerResearch.canResearch(msg.research)) {
                    playerResearch.setResearch(msg.research, ZonedDateTime.now(Constants.DEFAULT_TIME_ZONE))
                    playerResearch.sync(player, true)
                }
            } else {
                player.sendMessage(TranslatableComponent("message.afraidofthedark.arcane_journal.cheat_sheet_missing"))
            }
        }
    }

    companion object {
        private val CHEAT_SHEET = ItemStack(ModItems.ARCANE_JOURNAL).apply { ModItems.ARCANE_JOURNAL.setCheatSheet(this) }
    }
}