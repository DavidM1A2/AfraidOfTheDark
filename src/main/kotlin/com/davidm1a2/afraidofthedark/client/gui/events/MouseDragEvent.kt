package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents


class MouseDragEvent(source: AOTDGuiComponentWithEvents, val mouseX: Int, val mouseY: Int, val clickedButton: Int) : AOTDEvent(source)
