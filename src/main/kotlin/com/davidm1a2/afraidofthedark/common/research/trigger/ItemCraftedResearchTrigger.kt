package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.player.PlayerEvent
import kotlin.reflect.KClass

class ItemCraftedResearchTrigger : ResearchTrigger<PlayerEvent.ItemCraftedEvent, ItemCraftedResearchTriggerConfig>(ItemCraftedResearchTriggerConfig.CODEC) {
    override val type: KClass<PlayerEvent.ItemCraftedEvent> = PlayerEvent.ItemCraftedEvent::class

    init {
        setRegistryName(Constants.MOD_ID, "item_crafted")
    }

    override fun getAffectedPlayer(event: PlayerEvent.ItemCraftedEvent): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerEvent.ItemCraftedEvent, config: ItemCraftedResearchTriggerConfig): Boolean {
        val craftedStack = event.crafting
        return craftedStack.item == config.item && (config.count == null || config.count == craftedStack.count) && (config.data == null || config.data == craftedStack.tag)
    }
}