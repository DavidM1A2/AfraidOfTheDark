package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.event.custom.PlayerChangePowerSourceEvent
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.util.text.TranslatableComponent
import net.minecraftforge.eventbus.api.SubscribeEvent

class SpellPowerSourceHandler {
    @SubscribeEvent
    fun onPlayerChangePowerSourceEvent(event: PlayerChangePowerSourceEvent) {
        if (event.oldPowerSource != event.newPowerSource) {
            event.player.sendMessage(TranslatableComponent("message.afraidofthedark.spell.power_source_changed", event.newPowerSource.getName()))
        }
    }
}
