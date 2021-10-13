package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import kotlin.reflect.KClass

class UseItemResearchTrigger : ResearchTrigger<PlayerInteractEvent.RightClickItem, UseItemResearchTriggerConfig>(UseItemResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "use_item")
    }

    override fun getEventType(config: UseItemResearchTriggerConfig): KClass<PlayerInteractEvent.RightClickItem> {
        return PlayerInteractEvent.RightClickItem::class
    }

    override fun getAffectedPlayer(event: PlayerInteractEvent.RightClickItem, config: UseItemResearchTriggerConfig): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerInteractEvent.RightClickItem, config: UseItemResearchTriggerConfig): Boolean {
        val itemStack = event.itemStack
        return itemStack.item == config.item && (config.data == null || config.data == itemStack.tag)
    }
}