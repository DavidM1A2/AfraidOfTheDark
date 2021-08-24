package com.davidm1a2.afraidofthedark.common.utility

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.BoltEntry
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.entity.player.PlayerEntity

/**
 * Class used to assist in ordering the bolts and picking the next bolt in the list
 */
object BoltOrderHelper {
    /**
     * Gets the next valid bolt index after the current index. It might not be i+1 since the
     * pre-requisite research might not yet be reserched
     *
     * @param entityPlayer     The player that is advancing bolt types
     * @param currentBoltIndex The current bolt index
     * @return The new bolt index
     */
    fun getNextBoltIndex(entityPlayer: PlayerEntity, currentBoltIndex: Int): Int {
        @Suppress("NAME_SHADOWING")
        var currentBoltIndex = currentBoltIndex
        var preRequisite: Research?

        // The number of possible bolt types
        val boltTypeCount = ModRegistries.BOLTS.entries.size
        do {
            // Increment bolt type until we find one that is valid for the player holding the bow
            currentBoltIndex++
            if (currentBoltIndex >= boltTypeCount) {
                currentBoltIndex = 0
            }
            preRequisite = getBoltAt(currentBoltIndex).prerequisiteResearch
        } while (preRequisite != null && !entityPlayer.getResearch().isResearched(preRequisite))
        return currentBoltIndex
    }

    /**
     * Returns the bolt entry at a given index from the registry
     *
     * @param index The index to get from
     * @return The bolt entry at that index in the registry
     */
    fun getBoltAt(index: Int): BoltEntry {
        return ModRegistries.BOLTS.values.elementAt(index)
    }
}