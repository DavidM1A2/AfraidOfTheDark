package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import kotlin.reflect.KClass

class UseBlockResearchTrigger :
    ResearchTrigger<PlayerInteractEvent.RightClickBlock, UseBlockResearchTriggerConfig>(UseBlockResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "use_block")
    }

    override fun getEventType(config: UseBlockResearchTriggerConfig): KClass<PlayerInteractEvent.RightClickBlock> {
        return PlayerInteractEvent.RightClickBlock::class
    }

    override fun getAffectedPlayer(event: PlayerInteractEvent.RightClickBlock, config: UseBlockResearchTriggerConfig): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerInteractEvent.RightClickBlock, config: UseBlockResearchTriggerConfig): Boolean {
        val affectedBlockState = event.world.getBlockState(event.pos)
        return config.blockOrState.map({ affectedBlockState == it }, { affectedBlockState.block == it })
    }
}