package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellStates
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

/**
 * Handles the on server tick to update any existing spell states
 */
class SpellStateHandler {
    /**
     * Called every game tick on the server, updates all server wide spell data
     *
     * @param event The event containing server tick info
     */
    @SubscribeEvent
    fun onWorldTick(event: TickEvent.WorldTickEvent) {
        if (event.phase == TickEvent.Phase.START && event.type == TickEvent.Type.WORLD && event.side == LogicalSide.SERVER) {
            event.world.getSpellStates().tick()
        }
    }
}