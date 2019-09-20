package com.davidm1a2.afraidofthedark.common.capabilities.player.research;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries;
import com.davidm1a2.afraidofthedark.common.constants.ModSounds;
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.SyncResearch;
import com.davidm1a2.afraidofthedark.common.registry.research.Research;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the AOTD player research capability
 */
public class AOTDPlayerResearchImpl implements IAOTDPlayerResearch
{
    // A mapping of research to if that research is unlocked for a given user
    private Map<Research, Boolean> researchToUnlocked = new HashMap<>();

    /**
     * Initializes the hashmap to all researches not unlocked
     */
    public AOTDPlayerResearchImpl()
    {
        for (Research research : ModRegistries.RESEARCH)
            researchToUnlocked.put(research, false);
    }

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private boolean isServerSide(EntityPlayer entityPlayer)
    {
        return !entityPlayer.world.isRemote;
    }

    /**
     * Returns true if a research is researched by a player or false otherwise
     *
     * @param research The research to test
     * @return True if the research is researched, or false otherwise
     */
    @Override
    public boolean isResearched(Research research)
    {
        return this.researchToUnlocked.getOrDefault(research, false);
    }

    /**
     * Returns true if the user can research a research or false if not
     *
     * @param research The research to test
     * @return True if the player can research a given research or false otherwise
     */
    @Override
    public boolean canResearch(Research research)
    {
        Research preRequisite = research.getPreRequisite();
        if (preRequisite == null)
        {
            return true;
        }
        else
        {
            return !this.isResearched(research) && this.isResearched(preRequisite);
        }
    }

    /**
     * Sets a given research to be unlocked or not
     *
     * @param research   The research to unlock
     * @param researched If the research is researched or not
     */
    @Override
    public void setResearch(Research research, boolean researched)
    {
        this.researchToUnlocked.put(research, researched);
    }

    /**
     * Sets a given research to be unlocked or not and shows the player a popup that notifies them of the unlock
     *
     * @param research     The research to unlock
     * @param researched   If the research is researched or not
     * @param entityPlayer The player to alert of the research
     */
    @Override
    public void setResearchAndAlert(Research research, boolean researched, EntityPlayer entityPlayer)
    {
        this.setResearch(research, researched);
        if (!this.isServerSide(entityPlayer))
        {
            // Play the achievement sound and display the research
            entityPlayer.playSound(ModSounds.ACHIEVEMENT_UNLOCKED, 1.0f, 1.0f);
            AfraidOfTheDark.proxy.getResearchOverlay().displayResearch(research);
        }
    }

    /**
     * Syncronizes research between server and client
     *
     * @param entityPlayer The player to sync research to
     * @param notify       True if the player should be notified of any new researches, false otherwise
     */
    @Override
    public void sync(EntityPlayer entityPlayer, boolean notify)
    {
        // Send packets based on server side or client side
        if (this.isServerSide(entityPlayer))
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new SyncResearch(this.researchToUnlocked, notify), (EntityPlayerMP) entityPlayer);
        }
        else
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new SyncResearch(this.researchToUnlocked, notify));
        }
    }
}
