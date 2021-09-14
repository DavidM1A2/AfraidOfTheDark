package com.davidm1a2.afraidofthedark.common.research

import kotlin.math.cos
import kotlin.math.sin

// Wrapper used for positioning researches at load time
class ResearchWrapper(val research: Research) {
    var xPosition: Double = 0.0
    var yPosition: Double = 0.0
    var leftHandAngle: Double = 0.0
    var rightHandAngle: Double = 2 * Math.PI
    var outerRadius: Double = 0.0
    var parent: ResearchWrapper? = null
    val children = mutableListOf<ResearchWrapper>()

    // Updates all children's positions
    fun computeChildren(thickness: Double): ResearchWrapper {
        for (i in children.indices) {
            val child = children[i]
            child.outerRadius = this.outerRadius + thickness
            val sliceWidth = (rightHandAngle - leftHandAngle) / children.size
            child.leftHandAngle = this.leftHandAngle + i * sliceWidth
            child.rightHandAngle = child.leftHandAngle + sliceWidth
            child.computeChildren(thickness)
            child.computePos()
            child.applyPos()
        }
        return this
    }

    private fun computePos() {
        val radius = outerRadius
        val theta = (leftHandAngle + rightHandAngle)/2
        xPosition = radius * cos(theta)
        yPosition = radius * sin(theta)
    }

    fun applyPos(): Research {
        research.xPosition = xPosition
        research.yPosition = yPosition
        return research
    }
}