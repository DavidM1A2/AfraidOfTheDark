package com.DavidM1A2.afraidofthedark.common.event;

import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Class handling events to send players to and from their nightmare realm
 */
public class NightmareHandler
{
    /**
     * Called when the player sleeps in a bed, tests if they're drowsy and if so sends them to the nightmare realm
     *
     * @param event event containing player and world data
     */
    @SubscribeEvent
    public void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event)
    {

    }
}
