package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiGravity
import com.davidm1a2.afraidofthedark.client.gui.base.RelativePosition
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LineComponent
import java.awt.Color

class ResearchConnector(from: RelativePosition, to: RelativePosition): LineComponent(from, to, 0.1, Color.CYAN, AOTDGuiGravity.CENTER)