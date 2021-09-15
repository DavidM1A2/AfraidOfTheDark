package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.research.ResearchWrapper
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import kotlin.math.max

class ResearchPositionHandler {

    private val wrapperMap = mutableMapOf<Research, ResearchWrapper>()

    @SubscribeEvent
    fun initResearchPositions(event: FMLClientSetupEvent) {
        var longestChain = 1
        ModRegistries.RESEARCH.forEach {
            wrapperMap[it] = ResearchWrapper(it).apply { parent = if (it.preRequisite == null) null else wrapperMap.getOrDefault(it.preRequisite, null) }
            wrapperMap[it]!!.parent?.children?.add(wrapperMap[it]!!)
            longestChain = max(longestChain, calcDistFromRoot(wrapperMap[it]!!))
        }
        findRoot()?.computeChildren(1.0/(longestChain+1))?.applyPos()
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
        wrapperMap.values.forEach { if (it.parent == null) return it }
        return null
    }
}