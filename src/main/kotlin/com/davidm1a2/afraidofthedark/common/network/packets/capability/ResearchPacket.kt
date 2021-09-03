package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.research.Research

class ResearchPacket(
    internal val researchToUnlocked: Map<Research, Boolean>,
    internal val notifyNewResearch: Boolean
)