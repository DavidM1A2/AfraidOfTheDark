package com.DavidM1A2.afraidofthedark.common.event;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModPotions;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import net.minecraft.entity.player.EntityPlayer;
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
        EntityPlayer entityPlayer = event.getEntityPlayer();
        // Only process server side
        if (!entityPlayer.world.isRemote)
        {
            // If the player has a sleeping potion effect on and has the right researches send them to the nightmare
            if (entityPlayer.getActivePotionEffect(ModPotions.SLEEPING_POTION) != null)
            {
                IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
                // If the player can research the nightmare research do so
                if (playerResearch.canResearch(ModResearches.NIGHTMARE))
                {
                    playerResearch.setResearch(ModResearches.NIGHTMARE, true);
                    playerResearch.sync(entityPlayer, true);
                }

                // If the player has the nightmare research send them to the nightmare realm
                if (playerResearch.isResearched(ModResearches.NIGHTMARE))
                {
                    //entityPlayer.changeDimension(ModDimensions.VOID_CHEST.getId(), ModDimensions.NOOP_TELEPORTER);
                }
            }
        }
    }
}
