package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LineComponent
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import java.awt.Color

/**
 * A connector between research nodes used in the Journal Research Screen
 */
class ResearchConnector(from: Position, to: Position, private val toResearch: Research):
    LineComponent(from, to, from.avg(to), 5.0, Color.CYAN, Gravity.CENTER) {

    // The player's research for fast querying
    private val playerResearch = entityPlayer.getResearch()

    override var isVisible: Boolean
        get() = playerResearch.isResearched(this.toResearch) || playerResearch.canResearch(this.toResearch)
        set(value) {}
}