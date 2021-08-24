package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.research.Research

/**
 * This class has a different implementation on the logical server vs client, so declare an interface to be used in our proxy.
 */
interface ResearchOverlayHandler {
    fun displayResearch(research: Research)
}