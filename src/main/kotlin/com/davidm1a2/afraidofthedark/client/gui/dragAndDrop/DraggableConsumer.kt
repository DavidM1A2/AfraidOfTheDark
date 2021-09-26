package com.davidm1a2.afraidofthedark.client.gui.dragAndDrop

interface DraggableConsumer<T> {
    fun consume(data: Any)
}