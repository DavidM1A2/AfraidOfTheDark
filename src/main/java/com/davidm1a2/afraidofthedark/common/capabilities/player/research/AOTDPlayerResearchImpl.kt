package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.SyncResearch
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP

/**
 * Default implementation of the AOTD player research capability
 *
 * @property researchToUnlocked A mapping of research to if that research is unlocked for a given user
 */
class AOTDPlayerResearchImpl : IAOTDPlayerResearch
{
    private val researchToUnlocked: MutableMap<Research, Boolean> = mutableMapOf()

    /**
     * Initializes the hashmap to all researches not unlocked
     */
    init
    {
        ModRegistries.RESEARCH.valuesCollection.forEach { researchToUnlocked[it] = false }
    }

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private fun isServerSide(entityPlayer: EntityPlayer): Boolean
    {
        return !entityPlayer.world.isRemote
    }

    /**
     * Returns true if a research is researched by a player or false otherwise
     *
     * @param research The research to test
     * @return True if the research is researched, or false otherwise
     */
    override fun isResearched(research: Research): Boolean
    {
        return researchToUnlocked.getOrDefault(research, false)
    }

    /**
     * Returns true if the user can research a research or false if not
     *
     * @param research The research to test
     * @return True if the player can research a given research or false otherwise
     */
    override fun canResearch(research: Research): Boolean
    {
        val preRequisite = research.preRequisite
        return if (preRequisite == null)
        {
            true
        }
        else
        {
            !isResearched(research) && isResearched(preRequisite)
        }
    }

    /**
     * Sets a given research to be unlocked or not
     *
     * @param research   The research to unlock
     * @param researched If the research is researched or not
     */
    override fun setResearch(research: Research, researched: Boolean)
    {
        researchToUnlocked[research] = researched
    }

    /**
     * Sets a given research to be unlocked or not and shows the player a popup that notifies them of the unlock
     *
     * @param research     The research to unlock
     * @param researched   If the research is researched or not
     * @param entityPlayer The player to alert of the research
     */
    override fun setResearchAndAlert(research: Research, researched: Boolean, entityPlayer: EntityPlayer)
    {
        setResearch(research, researched)
        if (!isServerSide(entityPlayer))
        {
            // Play the achievement sound and display the research
            entityPlayer.playSound(ModSounds.ACHIEVEMENT_UNLOCKED, 1.0f, 1.0f)
            AfraidOfTheDark.proxy.researchOverlay!!.displayResearch(research)
        }
    }

    /**
     * Syncronizes research between server and client
     *
     * @param entityPlayer The player to sync research to
     * @param notify       True if the player should be notified of any new researches, false otherwise
     */
    override fun sync(entityPlayer: EntityPlayer, notify: Boolean)
    {
        // Send packets based on server side or client side
        if (isServerSide(entityPlayer))
        {
            AfraidOfTheDark.INSTANCE.packetHandler.sendTo(SyncResearch(researchToUnlocked, notify), entityPlayer as EntityPlayerMP)
        }
        else
        {
            AfraidOfTheDark.INSTANCE.packetHandler.sendToServer(SyncResearch(researchToUnlocked, notify))
        }
    }
}