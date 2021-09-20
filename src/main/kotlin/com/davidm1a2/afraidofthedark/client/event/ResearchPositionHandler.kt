package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class ResearchPositionHandler {
    private val wrapperMap = mutableMapOf<Research, ResearchWrapper>()

    @SubscribeEvent
    fun initResearchPositions(event: FMLClientSetupEvent) {
        // First pass to create all wrappers
        for (research in ModRegistries.RESEARCH) {
            wrapperMap[research] = ResearchWrapper(research)
        }
        // Second pass to link wrappers together
        var longestChain = 1
        for (research in ModRegistries.RESEARCH) {
            wrapperMap[research]!!.parent = research.preRequisite?.let { preReq -> wrapperMap[preReq] }
            wrapperMap[research]!!.parent?.children?.add(wrapperMap[research]!!)
            longestChain = max(longestChain, calcDistFromRoot(wrapperMap[research]!!))
        }
        // Compute spacing from the root node
        findRoot()?.apply {
            computeChildren(1.0 / (longestChain + 1))
            applyPos()
        }
    }

    private fun calcDistFromRoot(researchWrapper: ResearchWrapper): Int {
        var count = 0
        var cur = researchWrapper
        while (cur.parent != null) {
            cur = cur.parent!!
            count++
        }
        return count
    }

    private fun findRoot(): ResearchWrapper? {
        return wrapperMap.values.find { it.parent == null }
    }

    // Wrapper used for positioning researches at load time
    private class ResearchWrapper(val research: Research) {
        private var xPosition: Double = 0.0
        private var yPosition: Double = 0.0
        private var leftHandAngle: Double = 0.0
        private var rightHandAngle: Double = 2 * Math.PI
        private var outerRadius: Double = 0.0
        var parent: ResearchWrapper? = null
        val children = mutableListOf<ResearchWrapper>()
        private var weight = 1.0

        // Updates all children's positions
        fun computeChildren(thickness: Double) {
            computeWeight()
            val totalChildWeight = children.sumOf { it.weight }
            var curLeftAngle = leftHandAngle
            for (child in children) {
                child.outerRadius = this.outerRadius + thickness
                val sliceWidth = (rightHandAngle - leftHandAngle) * child.weight / totalChildWeight
                child.leftHandAngle = curLeftAngle
                curLeftAngle += sliceWidth
                child.rightHandAngle = curLeftAngle
                child.computeChildren(thickness)
                child.computePos()
                child.applyPos()
            }
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
            val theta = (leftHandAngle + rightHandAngle) / 2
            xPosition = radius * cos(theta)
            yPosition = radius * sin(theta)
        }

        fun applyPos() {
            research.xPosition = xPosition
            research.yPosition = yPosition
        }
    }
}