package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.network.packets.capability.ResearchPacket
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.time.ZonedDateTime

/**
 * Default implementation of the AOTD player research capability
 *
 * @constructor Initializes the hashmap to all researches not unlocked
 * @property researchToUnlocked A mapping of research to if that research is unlocked for a given user
 */
class PlayerResearch : IPlayerResearch {
    private val researchToUnlocked: MutableMap<Research, ZonedDateTime?> = mutableMapOf()

    init {
        ModRegistries.RESEARCH.values.forEach { researchToUnlocked[it] = null }
    }

    private fun isServerSide(entityPlayer: Player): Boolean {
        return !entityPlayer.level.isClientSide
    }

    override fun isResearched(research: Research): Boolean {
        return getResearchTime(research) != null
    }

    override fun getResearchTime(research: Research): ZonedDateTime? {
        return researchToUnlocked[research]
    }

    override fun canResearch(research: Research): Boolean {
        return !isResearched(research) && research.preRequisite?.let { isResearched(it) } ?: true
    }

    override fun setResearch(research: Research, researchTime: ZonedDateTime?) {
        researchToUnlocked[research] = researchTime
    }

    override fun sync(entityPlayer: Player, notify: Boolean) {
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(
                ResearchPacket(researchToUnlocked, notify),
                entityPlayer as ServerPlayer
            )
        }
    }
}