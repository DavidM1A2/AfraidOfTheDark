package com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors

import net.minecraft.nbt.INBT
import net.minecraft.nbt.IntNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import org.apache.commons.logging.LogFactory

/**
 * Default storage implementation for AOTD island visitors
 */
class AOTDWorldIslandVisitorsStorage : Capability.IStorage<IAOTDWorldIslandVisitors> {
    override fun writeNBT(capability: Capability<IAOTDWorldIslandVisitors>, instance: IAOTDWorldIslandVisitors, side: Direction?): INBT {
        return IntNBT(instance.getNumberOfVisitors())
    }

    override fun readNBT(capability: Capability<IAOTDWorldIslandVisitors>, instance: IAOTDWorldIslandVisitors, side: Direction?, nbt: INBT) {
        if (nbt is IntNBT) {
            instance.setNumberOfVisitors(nbt.int)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an IntNBT")
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(AOTDWorldIslandVisitorsStorage::class.java)
    }
}