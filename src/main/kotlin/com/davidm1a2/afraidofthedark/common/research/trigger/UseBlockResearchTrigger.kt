package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import kotlin.reflect.KClass

class UseBlockResearchTrigger :
    ResearchTrigger<PlayerInteractEvent.RightClickBlock, UseBlockResearchTriggerConfig>(UseBlockResearchTriggerConfig.CODEC) {
    override val type: KClass<PlayerInteractEvent.RightClickBlock> = PlayerInteractEvent.RightClickBlock::class

    init {
        setRegistryName(Constants.MOD_ID, "use_block")
    }

    override fun getAffectedPlayer(event: PlayerInteractEvent.RightClickBlock): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerInteractEvent.RightClickBlock, config: UseBlockResearchTriggerConfig): Boolean {
        val affectedBlockState = event.world.getBlockState(event.pos)
        return config.blockOrState.map({ affectedBlockState == it }, { affectedBlockState.block == it })
    }
}