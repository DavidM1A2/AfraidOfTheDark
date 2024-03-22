package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.player.PlayerEvent
import kotlin.reflect.KClass

class ItemCraftedResearchTrigger : ResearchTrigger<PlayerEvent.ItemCraftedEvent, ItemCraftedResearchTriggerConfig>(ItemCraftedResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "item_crafted")
    }

    override fun getEventType(config: ItemCraftedResearchTriggerConfig): KClass<PlayerEvent.ItemCraftedEvent> {
        return PlayerEvent.ItemCraftedEvent::class
    }

    override fun getAffectedPlayer(event: PlayerEvent.ItemCraftedEvent, config: ItemCraftedResearchTriggerConfig): Player? {
        return event.player
    }

    override fun shouldUnlock(player: Player, event: PlayerEvent.ItemCraftedEvent, config: ItemCraftedResearchTriggerConfig): Boolean {
        val craftedStack = event.crafting
        return craftedStack.item == config.item && (config.count == null || config.count == craftedStack.count) && (config.data == null || config.data == craftedStack.tag)
    }
}