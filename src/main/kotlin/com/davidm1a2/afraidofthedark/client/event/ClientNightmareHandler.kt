package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.inventory.ChestScreen
import net.minecraft.util.text.TranslatableComponent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ClientNightmareHandler {
    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        // Can't open an echest in the nightmare realm
        val player = Minecraft.getInstance().player
        if (player?.level?.dimension() == ModDimensions.NIGHTMARE_WORLD) {
            val gui = event.gui
            if (gui is ChestScreen) {
                val title = gui.title
                if (title is TranslatableComponent && title.key.contains("enderchest")) {
                    // Close the chest and don't show the gui
                    gui.menu.container.stopOpen(player)
                    event.isCanceled = true
                    player.sendMessage(TranslatableComponent("message.afraidofthedark.nightmare.enderchest"))
                }
            }
        }
    }
}