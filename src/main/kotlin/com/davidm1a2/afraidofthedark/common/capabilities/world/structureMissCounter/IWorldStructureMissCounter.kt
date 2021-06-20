package com.davidm1a2.afraidofthedark.common.capabilities.world.structureMissCounter

import net.minecraft.world.gen.feature.structure.Structure

/**
 * Capability interface to store structure counts in a world used in world generation
 */
interface IWorldStructureMissCounter {
    /**
     * Increments the missed counter by some amount
     *
     * @param structure The structure to increment count for
     * @param amount The amount to increment by
     */
    fun increment(structure: Structure<*>, amount: Int = 1)

    /**
     * Gets the current missed counter for a structure
     *
     * @param structure The structure to get the count for
     * @return The number of times the structure failed to generate
     */
    fun get(structure: Structure<*>): Int

    /**
     * Resets the missed count for a structure to 0
     *
     * @param structure The structure to reset the count for
     */
    fun reset(structure: Structure<*>)
}