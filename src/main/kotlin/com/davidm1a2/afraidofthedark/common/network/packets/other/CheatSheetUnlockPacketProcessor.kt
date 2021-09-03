package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

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
                    playerResearch.setResearch(msg.research, true)
                    playerResearch.sync(player, true)
                }
            } else {
                player.sendMessage(TranslationTextComponent("message.afraidofthedark.journal.cheat_sheet_missing"), player.uuid)
            }
        }
    }

    companion object {
        private val CHEAT_SHEET = ItemStack(ModItems.JOURNAL).apply { ModItems.JOURNAL.setCheatSheet(this) }
    }
}