package com.davidm1a2.afraidofthedark.client.gui.dragAndDrop

import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane

interface DraggableProducer<T> {
    fun produce(): T
    fun getIcon(): ImagePane
}