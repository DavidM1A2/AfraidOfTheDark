package com.davidm1a2.afraidofthedark.client.gui.base

data class AOTDGuiSpacing(val topPx: Int, val botPx: Int, val leftPx: Int, val rightPx: Int) {
    constructor() : this(0, 0, 0, 0)
    constructor(uniformPx: Int) : this(uniformPx, uniformPx, uniformPx, uniformPx)
    val horizPx: Int
        get() = leftPx + rightPx
    val vertPx: Int
        get() = topPx + botPx
}