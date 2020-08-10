package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.world.SpellStateData
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent

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
    fun onServerTick(event: ServerTickEvent) {
        if (event.phase == TickEvent.Phase.START && event.type == TickEvent.Type.SERVER && event.side == LogicalSide.SERVER) {
            // This can be null on the first server tick before everything is initialized
            SpellStateData.get()?.update()
        }
    }
}