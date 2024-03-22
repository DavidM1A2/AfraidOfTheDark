package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.player.PlayerEvent
import kotlin.reflect.KClass

class ItemSmeltedResearchTrigger : ResearchTrigger<PlayerEvent.ItemSmeltedEvent, ItemSmeltedResearchTriggerConfig>(ItemSmeltedResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "item_smelted")
    }

    override fun getEventType(config: ItemSmeltedResearchTriggerConfig): KClass<PlayerEvent.ItemSmeltedEvent> {
        return PlayerEvent.ItemSmeltedEvent::class
    }

    override fun getAffectedPlayer(event: PlayerEvent.ItemSmeltedEvent, config: ItemSmeltedResearchTriggerConfig): Player? {
        return event.player
    }

    override fun shouldUnlock(player: Player, event: PlayerEvent.ItemSmeltedEvent, config: ItemSmeltedResearchTriggerConfig): Boolean {
        val smeltedStack = event.smelting
        return smeltedStack.item == config.item && (config.data == null || config.data == smeltedStack.tag)
    }
}