package com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.registry.research.Research

class ResearchPacket(
    internal val researchToUnlocked: Map<Research, Boolean>,
    internal val notifyNewResearch: Boolean
)