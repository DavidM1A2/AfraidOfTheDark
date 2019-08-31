package com.DavidM1A2.afraidofthedark.common.event;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.SpellStateData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Handles the on server tick to update any existing spell states
 */
public class SpellStateHandler
{
    /**
     * Called every game tick on the server, updates all server wide spell data
     *
     * @param event The event containing server tick info
     */
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && event.type == TickEvent.Type.SERVER && event.side == Side.SERVER)
        {
            // Doesn't matter what world we use, SpellStateData is global
            SpellStateData.get(DimensionManager.getWorld(0)).update();
        }
    }
}
