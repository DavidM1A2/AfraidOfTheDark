package com.davidm1a2.afraidofthedark.common.capabilities.world.structureMissCounter

import net.minecraft.world.gen.feature.structure.Structure
import java.util.concurrent.ConcurrentHashMap

class WorldStructureMissCounter : IWorldStructureMissCounter {
    private val missCounter = ConcurrentHashMap<Structure<*>, Int>()

    override fun increment(structure: Structure<*>, amount: Int) {
        missCounter.compute(structure) { _, currentAmount -> currentAmount?.plus(amount) ?: amount }
    }

    override fun get(structure: Structure<*>): Int {
        return missCounter[structure] ?: 0
    }

    override fun reset(structure: Structure<*>) {
        missCounter[structure] = 0
    }
}