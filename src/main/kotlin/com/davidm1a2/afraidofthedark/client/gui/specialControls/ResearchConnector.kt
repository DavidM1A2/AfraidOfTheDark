package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.layout.GuiGravity
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativePosition
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LineComponent
import java.awt.Color

class ResearchConnector(from: RelativePosition, to: RelativePosition): LineComponent(from, to, 5.0, Color.CYAN, GuiGravity.CENTER)