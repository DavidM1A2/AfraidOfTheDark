package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents

/**
 * Class representing any AOTD gui events that have to do with the mouse scrolling
 *
 * @constructor initializes the scroll distance and source
 * @param source         The control that fired the event
 * @param scrollDistance The distance the scroll wheel has moved, + values indicate forward scroll, - values indicate negative scroll
 */
class AOTDMouseScrollEvent(source: AOTDGuiComponentWithEvents, val scrollDistance: Int) : AOTDEvent(source)
