package com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors

import java.util.concurrent.atomic.AtomicInteger

class AOTDWorldIslandVisitorsImpl : IAOTDWorldIslandVisitors {
    private val uniqueVisitors = AtomicInteger(-1)

    override fun addAndGetNewVisitor(): Int {
        return uniqueVisitors.incrementAndGet()
    }

    override fun setNumberOfVisitors(visitorCount: Int) {
        uniqueVisitors.set(visitorCount)
    }

    override fun getNumberOfVisitors(): Int {
        return uniqueVisitors.get()
    }
}