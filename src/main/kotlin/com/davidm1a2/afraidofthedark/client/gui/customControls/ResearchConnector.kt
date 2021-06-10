package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.layout.GuiGravity
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativePosition
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LineComponent
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import java.awt.Color

/**
 * A connector between research nodes used in the Journal Research Screen
 */
class ResearchConnector(from: RelativePosition, to: RelativePosition, private val toResearch: Research):
    LineComponent(from, to, 5.0, Color.CYAN, GuiGravity.CENTER) {

    // The player's research for fast querying
    private val playerResearch = entityPlayer.getResearch()

    override var isVisible: Boolean
        get() = playerResearch.isResearched(this.toResearch) || playerResearch.canResearch(this.toResearch)
        set(value) {}
}