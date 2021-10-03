package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.research.Research
import java.time.ZonedDateTime

class ResearchPacket(
    internal val researchToUnlocked: Map<Research, ZonedDateTime?>,
    internal val notifyNewResearch: Boolean
)