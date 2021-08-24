package com.davidm1a2.afraidofthedark.common.item.telescope

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.research.Research

/**
 * Advanced telescope item used to track meteors. Has an accuracy of 50 blocks
 */
class AdvancedTelescopeItem : TelescopeBaseItem(50, "advanced_telescope") {
    /**
     * Gets the required research to use this item
     *
     * @return A research
     */
    override fun getRequiredResearch(): Research {
        return ModResearches.OPTICS
    }
}