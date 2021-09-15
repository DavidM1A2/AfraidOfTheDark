package com.davidm1a2.afraidofthedark.common.research

import kotlin.math.cos
import kotlin.math.max
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
    var weight = 1.0

    // Updates all children's positions
    fun computeChildren(thickness: Double): ResearchWrapper {
        computeWeight()
        val totalChildWeight = children.stream().mapToDouble { it.weight }.sum()
        var curLeftAngle = leftHandAngle
        for (i in children.indices) {
            val child = children[i]
            child.outerRadius = this.outerRadius + thickness
            val sliceWidth = (rightHandAngle - leftHandAngle) * child.weight / totalChildWeight
            child.leftHandAngle = curLeftAngle
            curLeftAngle += sliceWidth
            child.rightHandAngle = curLeftAngle
            child.computeChildren(thickness)
            child.computePos()
            child.applyPos()
        }
        return this
    }

    private fun computeWeight() {
        val weightPerChild = 0.65
        val weightRetained = 0.15
        this.weight = 1.0 - weightPerChild
        children.forEach { it.computeWeight(); this.weight += weightPerChild + weightRetained * (it.weight - 1.0) }
        this.weight = max(weight, 1.0)
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