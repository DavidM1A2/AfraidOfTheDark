package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiComponentWithEvents

/**
 * Class representing any AOTD gui events that have to do with the mouse scrolling
 */
class MouseScrollEvent(source: AOTDGuiComponentWithEvents, val scrollDistance: Int) : AOTDEvent(source)
