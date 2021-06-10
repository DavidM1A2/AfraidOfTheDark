package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiComponentWithEvents

/**
 * Class representing a AOTD gui event in which the mouse is dragged
 */
class MouseDragEvent(source: AOTDGuiComponentWithEvents, val mouseX: Int, val mouseY: Int, val clickedButton: Int) : AOTDEvent(source)
