package com.davidm1a2.afraidofthedark.client.gui.standardControls

import kotlin.math.roundToInt

/**
 * Basically the same as the FIT_TO_PARENT version of the ImagePane, but without the actual image
 */
open class RatioPane(private val widthRatio: Int, private val heightRatio: Int) : AOTDPane() {

    override fun negotiateDimensions(width: Double, height: Double) {
        val squareWidth = width / widthRatio
        val squareHeight = height / heightRatio
        val squareMin = squareWidth.coerceAtMost(squareHeight)
        this.width = (squareMin * widthRatio).roundToInt()
        this.height = (squareMin * heightRatio).roundToInt()

        // Reset the inbounds flag
        inBounds = true
    }

}